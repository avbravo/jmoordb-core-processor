package com.jmoordb.core.processor;

import com.jmoordb.core.processor.internal.ClassProccessorAux;
import com.jmoordb.core.processor.internal.MethodProcessorAux;
import com.jmoordb.core.processor.model.RepositoryBasicFieldInfo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import com.jmoordb.core.annotation.RepositoryBasic;

@SupportedAnnotationTypes(
        {"com.jmoordb.core.annotation.RepositoryBasic"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryBasicProcessor extends AbstractProcessor {

    // <editor-fold defaultstate="collapsed" desc=" boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)">
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        if (annotations.size() == 0) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RepositoryBasic.class);

        List<String> uniqueIdCheckList = new ArrayList<>();

        for (Element element : elements) {
            RepositoryBasic repository = element.getAnnotation(RepositoryBasic.class);

            if (element.getKind() != ElementKind.INTERFACE) {
                error("The annotation @RepositoryBasic can only be applied on interfaces: ",
                        element);

            } else {
                boolean error = false;

                if (uniqueIdCheckList.contains(repository.entity())) {
                    error("RepositoryBasic has should be uniquely defined", element);
                    error = true;
                }

                error = !checkIdValidity(repository.entity(), element);

                if (!error) {
                    uniqueIdCheckList.add(repository.entity());
                    try {
                        generateClass(repository, element);
                    } catch (Exception e) {
                        error(e.getMessage(), null);
                    }
                }
            }
        }
        return false;
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="generateClass(Repository repository, Element element)">
    private void generateClass(RepositoryBasic repository, Element element)
            throws Exception {

        String pkg = getPackageName(element);

        //delegate some processing to our FieldInfo class
//        FieldInfo fieldInfo = FieldInfo.get(element);
        RepositoryBasicFieldInfo fieldInfo = RepositoryBasicFieldInfo.get(element);

        //the target interface name
        String interfaceName = getTypeName(element);

        //using our ClassProccessor to delegate most of the string appending there
        ClassProccessorAux implClass = new ClassProccessorAux();
        implClass.definePackage(pkg);

/*
Import
         */
        implClass.addEditorFoldStart("imports");
        if (!repository.jakarta()) {
            /*
            Java EE
             */
            implClass.addImport("javax.enterprise.context.ApplicationScoped");
            implClass.addImport("javax.inject.Inject");
            implClass.addImport("javax.json.bind.Jsonb");
            implClass.addImport("javax.json.bind.JsonbBuilder");
        } else {
            /**
             * Jakarta EE
             */
            implClass.addImport("jakarta.enterprise.context.ApplicationScoped");
            implClass.addImport("jakarta.inject.Inject");
            implClass.addImport("jakarta.json.bind.Jsonb");
            implClass.addImport("jakarta.json.bind.JsonbBuilder");

        }
        /**
         * Microprofile
         */
        implClass.addImport("org.eclipse.microprofile.config.Config");
        implClass.addImport("org.eclipse.microprofile.config.inject.ConfigProperty");
        /**
         * MongoDB
         */
        implClass.addImport("com.mongodb.client.MongoDatabase;");
        implClass.addImport("static com.mongodb.client.model.Filters.eq");
        implClass.addImport("com.mongodb.client.MongoClient");
        implClass.addImport("com.mongodb.client.MongoCollection");
        implClass.addImport("com.mongodb.client.MongoCursor");

        implClass.addImport("java.util.ArrayList");
        implClass.addImport("java.util.List");
        implClass.addImport("java.util.Optional");
        implClass.addEditorFoldEnd();

        /**
         * Anotaciones
         */
        implClass.addAnnotations("@ApplicationScoped");
        /*
        Clase
         */
        implClass.defineClass("public class ", interfaceName + "Impl", " implements " + interfaceName);

        /**
         * Inject
         */
        implClass.addEditorFoldStart("inject");
        implClass.addComment("Microprofile Config");

        implClass.addInject("private Config config");
        implClass.addInject("MongoClient mongoClient");
        implClass.addEditorFoldEnd();

//        //nested builder class
        ClassProccessorAux builder = null;
        String builderClassName = null;
//
//        if (repository.jakarta()) {
//            builder = new ClassProccessor();
//            builder.defineClass("public static class",
//                    builderClassName = repository.entity() + "Builder", null);
//        }

        //adding class fields
        implClass.addFields(fieldInfo.getFields());
        if (builder != null) {
            builder.addFields(fieldInfo.getFields());
        }

        //adding constructor with mandatory fields
        implClass.addConstructor(builder == null ? "public" : "private",
                fieldInfo.getMandatoryFields());
        if (builder != null) {
            builder.addConstructor("private", fieldInfo.getMandatoryFields());
        }

        //generate methods
        for (Map.Entry<String, String> entry : fieldInfo.getFields().entrySet()) {
            String name = entry.getKey();
            String type = entry.getValue();
            boolean mandatory = fieldInfo.getMandatoryFields().contains(name);

            implClass.createGetterForField(name);

            //if no builder generation specified then crete setters for non mandatory fields
            if (builder == null && !mandatory) {
                implClass.createSetterForField(name);
            }

            if (builder != null && !mandatory) {
                builder.addMethod(new MethodProcessorAux()
                        .defineSignature("public", false, builderClassName)
                        .name(name)
                        .addParam(type, name)
                        .defineBody(" this." + name + " = " + name + ";"
                                + ClassProccessorAux.LINE_BREAK
                                + " return this;"
                        )
                );
            }
        }

        if (builder != null) {

            //generate create() method of the Builder class
            MethodProcessorAux createMethod = new MethodProcessorAux()
                    .defineSignature("public", true, builderClassName)
                    .name("create");

            String paramString = "(";
            int i = 0;
            for (String s : fieldInfo.getMandatoryFields()) {
                createMethod.addParam(fieldInfo.getFields().get(s), s);
                paramString += (i != 0 ? ", " : "") + s;
                i++;
            }
            paramString += ");";

            createMethod.defineBody("return new " + builderClassName
                    + paramString);

            builder.addMethod(createMethod);

            //generate build() method of the builder class.
            MethodProcessorAux buildMethod = new MethodProcessorAux()
                    .defineSignature("public", false, repository.entity())
                    .name("build");
            StringBuilder buildBody = new StringBuilder();
            buildBody.append(repository.entity())
                    .append(" a = new ")
                    .append(repository.entity())
                    .append(paramString)
                    .append(ClassProccessorAux.LINE_BREAK);
            for (String s : fieldInfo.getFields().keySet()) {
                if (fieldInfo.getMandatoryFields().contains(s)) {
                    continue;
                }
                buildBody.append("a.")
                        .append(s)
                        .append(" = ")
                        .append(s)
                        .append(";")
                        .append(ClassProccessorAux.LINE_BREAK);
            }
            buildBody.append("return a;")
                    .append(ClassProccessorAux.LINE_BREAK);
            buildMethod.defineBody(buildBody.toString());

            builder.addMethod(buildMethod);
            implClass.addNestedClass(builder);

        }
        //finally generate class via Filer
//        generateClass(pkg + "." + repository.entity(), implClass.end());
        generateClass(pkg + "." + interfaceName + "Impl", implClass.end());
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String getPackageName(Element element)">

    private String getPackageName(Element element) {
        List<PackageElement> packageElements
                = ElementFilter.packagesIn(Arrays.asList(element.getEnclosingElement()));

        Optional<PackageElement> packageElement = packageElements.stream().findAny();
        return packageElement.isPresent()
                ? packageElement.get().getQualifiedName().toString() : null;

    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="generateClass(String qfn, String end)">
    private void generateClass(String qfn, String end) throws IOException {
        
                  
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
        Writer writer = sourceFile.openWriter();
        writer.write(end);
        writer.close();
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="boolean checkIdValidity(String name, Element e)">
    /**
     * Checking if the class to be generated is a valid java identifier Also the
     * name should be not same as the target interface
     */
    private boolean checkIdValidity(String name, Element e) {
        boolean valid = true;
        for (int i = 0; i < name.length(); i++) {
            if (i == 0 ? !Character.isJavaIdentifierStart(name.charAt(i))
                    : !Character.isJavaIdentifierPart(name.charAt(i))) {
                error("Repository #as should be valid java "
                        + "identifier for code generation: " + name, e);
                valid = false;
            }
        }
        if (name.equals(getTypeName(e))) {
            error("AutoImplement#as should be different than the Interface name ", e);
        }
        return valid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String getTypeName(Element e)">
    /**
     * Get the simple name of the TypeMirror
     */
    private static String getTypeName(Element e) {
        TypeMirror typeMirror = e.asType();
        String[] split = typeMirror.toString().split("\\.");
        return split.length > 0 ? split[split.length - 1] : null;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="error(String msg, Element e)">
    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
    // </editor-fold>
    
    
}
