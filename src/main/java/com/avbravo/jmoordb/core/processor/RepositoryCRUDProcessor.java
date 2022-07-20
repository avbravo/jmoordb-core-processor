package com.avbravo.jmoordb.core.processor;

import com.avbravo.jmoordb.core.processor.internal.ClassProccessorAux;
import com.avbravo.jmoordb.core.processor.internal.MethodProcessorAux;

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
import com.avbravo.jmoordb.core.annotation.RepositoryCRUD;
import static com.avbravo.jmoordb.core.annotation.app.MyAnnotationTypeProcessor.mirror;
import com.avbravo.jmoordb.core.processor.model.RepositoryCRUDFieldInfo;
import com.avbravo.jmoordb.core.util.Util;
import java.lang.reflect.Field;

@SupportedAnnotationTypes(
        {"com.avbravo.jmoordb.core.annotation.RepositoryCRUD"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryCRUDProcessor extends AbstractProcessor {

    // <editor-fold defaultstate="collapsed" desc=" boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)">
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            //testMsg("--------------------[RepositoryCRUD.process annotations =" + annotations + "]--------------", true);
            if (annotations.size() == 0) {
                return false;
            }

            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RepositoryCRUD.class);

            List<String> uniqueIdCheckList = new ArrayList<>();

            for (Element element : elements) {
                RepositoryCRUD repository = element.getAnnotation(RepositoryCRUD.class);
                //testMsg("{ for Element element : elements}", false);
                /**
                 * Analizo el tipo Objett
                 */

                
                TypeMirror type = mirror(repository::entity);
                if (type == null) {
                    //testMsg(">>>>>>>>>>> type== null", false);
                } else {
                    //testMsg("----------Search fields", false);
                    Field[] allFields = type.getClass().getDeclaredFields();
                    for (Field field : allFields) {
//        for(Field field:type.getClass().getDeclaredFields())){
                        //testMsg(":::field " + field.toGenericString() + "campo " + field.getName(), false);
                    }
                    //testMsg("----------- pase el proceso.....", false);
                }

                //testMsg("----------[element element.getKind() " + element.getKind().name(), false);
                if (element.getKind() != ElementKind.INTERFACE) {
                    //testMsg("--->>>> paso 0.0", false);
                    error("The annotation @Repository can only be applied on interfaces: ",
                            element);

                } else {
                    boolean error = false;
                    //testMsg(">>>>>>>>>>>paso 1", false);
                    //testMsg("---------------[type " + type.toString(), false);
                    //testMsg("---------------[type.getKind() " + type.getKind().name(), false);
                    //testMsg("---------------[type.type.getClass().getName() " + type.getClass().getName(), false);
//                    System.out.println(">>>repository.entity()"+repository.entity());
//                    if (uniqueIdCheckList.contains(repository.entity())) {
                    /**
                     * Obtener el nombre de la entidad
                     */
                    String nameOfEntity = Util.nameOfFileInPath(type.toString());
                    //testMsg("---------------{ nameOfEntity " + nameOfEntity, false);
                    if (uniqueIdCheckList.contains(nameOfEntity)) {
//                    if (uniqueIdCheckList.contains(repository.entity())) {
                        //testMsg("uniqueIdCheckList.contains(nameOfEntity)", false);
                        error("Repository has should be uniquely defined", element);
                        error = true;
                    }
                    //testMsg("|>>>>>>>>>>>paso 2", false);

//                    error = !checkIdValidity(repository.entity().getName(), element);
                    error = !checkIdValidity(nameOfEntity, element);
                    //testMsg("|>>>>>>>>>>>paso 3 error= " + error, false);
                    if (!error) {
                        //testMsg(">>>>>>>>>>>paso 4", false);
//                        uniqueIdCheckList.add(repository.entity().getName());
                        uniqueIdCheckList.add(nameOfEntity);
                        try {
                            //testMsg(">>>>>>>>>>>paso 5 voy a generar la clase..", false);

                            generateClass(repository, element);
                            //testMsg(">>>>>>>>>>>paso 6", false);
                        } catch (Exception e) {
                            //testMsg(">>>>>>>>>> paso 6.1", false);
                            error(e.getMessage(), null);
                        }
                    }
                }
            }
            //testMsg("-----------------------------[end process]---------------------", false);
        } catch (Exception e) {
            System.out.println("-----------------------------------------------------------");

            System.out.println("RepositoryProcessor.process() " + e.getLocalizedMessage());
            System.out.println("-----------------------------------------------------------");
        }
        return false;
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="generateClass(Repository repository, Element element)">
    private void generateClass(RepositoryCRUD repository, Element element)
            throws Exception {
        try {
            //testMsg("generateClass(Repository repository, Element element)", true);

            String pkg = getPackageName(element);

            //delegate some processing to our FieldInfo class
//        FieldInfo fieldInfo = FieldInfo.get(element);
            RepositoryCRUDFieldInfo fieldInfo = RepositoryCRUDFieldInfo.get(element);

            //the target interface name
            String interfaceName = getTypeName(element);

            //using our ClassProccessor to delegate most of the string appending there
            ClassProccessorAux implClass = new ClassProccessorAux();
            implClass.definePackage(pkg);
            //testMsg("imports", false);
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
            //testMsg("if (builder != null)", false);
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
                //testMsg("MethodProcessorAux buildMethod = new MethodProcessorAux()", false);
                MethodProcessorAux buildMethod = new MethodProcessorAux()
                        .defineSignature("public", false, repository.entity().getName())
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
            //testMsg("generateClass(pkg + \".\" + interfaceName + \"Impl\", implClass.end())", false);
            generateClass(pkg + "." + interfaceName + "Impl", implClass.end());
        } catch (Exception e) {
            System.out.println("RepositoryCRUD.generateClass() " + e.getLocalizedMessage());
        }
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
        try {

            //testMsg("RepositoryBasicProcessor.generateClass(String qfn , String end) =  " + qfn + " " + end, true);
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
            Writer writer = sourceFile.openWriter();
            writer.write(end);
            writer.close();
            //testMsg("writer.close.()", false);
        } catch (Exception e) {
            System.out.println("RepositoryCRUD.generateClass() " + e.getLocalizedMessage());
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="boolean checkIdValidity(String name, Element e)">
    /**
     * Checking if the class to be generated is a valid java identifier Also the
     * name should be not same as the target interface
     */
    private boolean checkIdValidity(String name, Element e) {
        boolean valid = true;
        try {

            //testMsg("checkIdValidity(String name, Element e)=" + name, true);

            for (int i = 0; i < name.length(); i++) {
                if (i == 0 ? !Character.isJavaIdentifierStart(name.charAt(i))
                        : !Character.isJavaIdentifierPart(name.charAt(i))) {
                    error("Repository $as should be valid java "
                            + "identifier for code generation: " + name, e);
                    //testMsg("checkIdValidity .pas_1", false);

                    valid = false;
                }
            }
            if (name.equals(getTypeName(e))) {
                error("AutoImplement $as should be different than the Interface name ", e);
                //testMsg("checkIdValidity.pas_2", false);
            }
        } catch (Exception ex) {
            System.out.println("RepositoryCRUD.checkIdValidity() " + ex.getLocalizedMessage());
        }
        //testMsg("valid = " + valid, false);
        return valid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String getTypeName(Element e)">
    /**
     * Get the simple name of the TypeMirror
     */
    private String getTypeName(Element e) {
        //testMsg("String getTypeName(Element e)", true);
        TypeMirror typeMirror = e.asType();
        String[] split = typeMirror.toString().split("\\.");
        try {

        } catch (Exception ex) {
            System.out.println("RepositoryCRUD.getTypeName() " + ex.getLocalizedMessage());
        }
        return split.length > 0 ? split[split.length - 1] : null;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="error(String msg, Element e)">
    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
    // </editor-fold>

    private void testMsg(String msg, Boolean marco) {
        String TAB = "    ";
        if (marco) {
            System.out.println("...........................................");
        }

        System.out.println(TAB + msg);

        if (marco) {
            System.out.println(".............................,,,,,...........");
        }
    }
}
