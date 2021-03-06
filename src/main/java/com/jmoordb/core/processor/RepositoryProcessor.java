package com.jmoordb.core.processor;

import com.jmoordb.core.processor.internal.ClassProccessorAux;
import com.jmoordb.core.processor.internal.MethodProcessorAux;

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
import com.jmoordb.core.annotation.repository.Repository;

import static com.jmoordb.core.annotation.app.MyAnnotationTypeProcessor.mirror;
import com.jmoordb.core.processor.model.RepositoryMethodInfo;
import com.jmoordb.core.util.Test;
import com.jmoordb.core.util.Util;
import java.lang.reflect.Field;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

@SupportedAnnotationTypes(
        {"com.jmoordb.core.annotation.repository.Repository"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryProcessor extends AbstractProcessor {

    private Messager messager;

    // <editor-fold defaultstate="collapsed" desc=" boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)">
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            testMsg("Iniciando proceso de analisis", true);

            if (annotations.size() == 0) {
                return false;
            }
            /**
             * Lee los elementos que tengan la anotacion @Repository
             */
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Repository.class);

            List<String> uniqueIdCheckList = new ArrayList<>();

            for (Element element : elements) {
                Repository repository = element.getAnnotation(Repository.class);

                /**
                 * Analizo Metodos
                 */
//                testMsg("              [inicio evaluando @Query]", true);
//                Set<? extends Element> annotatedElementsQuery = roundEnv.getElementsAnnotatedWith(Query.class);
//                
//
//                for (Element elementQuery : annotatedElementsQuery) {
//                    analizarMetodosQuery(elementQuery);
//                }
//
//                testMsg("              [fin  evaluando @Query]", true);
                /**
                 * Fin de Analizo Metodos
                 */
                /**
                 * Analizo el tipo Objett
                 */
                testMsg("              [evaluando Anotacion]", true);

                TypeMirror type = mirror(repository::entity);
                if (type == null) {
                    testMsg("type== null", false);
                } else {
                    //   testMsg("----------[Search fields[-------------------", false);
//                    Field[] allFields = type.getClass().getDeclaredFields();
//                    
//                     
//                   
//                    for (Field field : allFields) {
//                        //testMsg(":::field " + field.toGenericString() + "campo " + field.getName(), false);
//                    }
                }

                if (element.getKind() != ElementKind.INTERFACE) {

                    error("The annotation @Repository can only be applied on interfaces: ",
                            element);

                } else {
                    boolean error = false;

                    testMsg("                 [type " + type.toString(), false);

                    /**
                     * Obtener el nombre de la entidad
                     */
                    String nameOfEntity = Util.nameOfFileInPath(type.toString());
                    testMsg("                 [ nameOfEntity " + nameOfEntity + "]", false);
                    if (uniqueIdCheckList.contains(nameOfEntity)) {
                        error("Repository has should be uniquely defined", element);
                        error = true;
                    }

                    error = !checkIdValidity(nameOfEntity, element);
                    if (!error) {
                        uniqueIdCheckList.add(nameOfEntity);
                        try {

                            generateClass(repository, element);

                        } catch (Exception e) {
                            error(e.getMessage(), null);
                        }
                    }
                }
            }
            testMsg("Proceso de analisis finalizado", true);
        } catch (Exception e) {
            System.out.println("-----------------------------------------------------------");

            System.out.println("RepositoryProcessor.process() " + e.getLocalizedMessage());
            System.out.println("-----------------------------------------------------------");
        }
        return false;
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="generateClass(Repository repository, Element element)">
    private void generateClass(Repository repository, Element element)
            throws Exception {
        try {
            //   testMsg("[generateClass(Repository repository, Element element)]", true);

            String pkg = getPackageName(element);
            //the target interface name
            String interfaceName = getTypeName(element);

            /**
             * Procesa el contenido de la interface
             */
            RepositoryMethodInfo fieldInfo = RepositoryMethodInfo.get(element, messager);

            //using our ClassProccessor to delegate most of the string appending there
            ClassProccessorAux classProccessorAux = new ClassProccessorAux();
            classProccessorAux.definePackage(pkg);
            //  testMsg("imports", false);
            /*
Import
             */
            classProccessorAux.addEditorFoldStart("imports");
            classProccessorAux.generateImport(repository);

            classProccessorAux.addEditorFoldEnd();

            /**
             * Anotaciones
             */
            classProccessorAux.addAnnotations("@ApplicationScoped");
            /*
        Clase
             */
            classProccessorAux.defineClass("public class ", interfaceName + "Impl", " implements " + interfaceName);

            /**
             * Inject
             */
            classProccessorAux.addEditorFoldStart("inject");
            classProccessorAux.addComment("Microprofile Config");

            classProccessorAux.addInject("private Config config");
            classProccessorAux.addInject("MongoClient mongoClient");
            classProccessorAux.addEditorFoldEnd();

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
            classProccessorAux.addFields(fieldInfo.getFields());
            if (builder != null) {
                builder.addFields(fieldInfo.getFields());
            }

            //adding constructor with mandatory fields
            classProccessorAux.addConstructor(builder == null ? "public" : "private",
                    fieldInfo.getMandatoryFields());
            if (builder != null) {
                builder.addConstructor("private", fieldInfo.getMandatoryFields());
            }

            //generate methods
            for (Map.Entry<String, String> entry : fieldInfo.getFields().entrySet()) {
                String name = entry.getKey();
                String type = entry.getValue();
                boolean mandatory = fieldInfo.getMandatoryFields().contains(name);

                classProccessorAux.createGetterForField(name);

                //if no builder generation specified then crete setters for non mandatory fields
                if (builder == null && !mandatory) {
                    classProccessorAux.createSetterForField(name);
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
                //  testMsg("MethodProcessorAux buildMethod = new MethodProcessorAux()", false);
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
                classProccessorAux.addNestedClass(builder);

            }
            //finally generate class via Filer
//        generateClass(pkg + "." + repository.entity(), implClass.end());
            //  testMsg("generateClass(pkg + \".\" + interfaceName + \"Impl\", implClass.end())", false);
            generateClass(pkg + "." + interfaceName + "Impl", classProccessorAux.end());
        } catch (Exception e) {
            System.out.println("Repository.generateClass() " + e.getLocalizedMessage());
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

            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
            Writer writer = sourceFile.openWriter();
            writer.write(end);
            writer.close();

        } catch (Exception e) {
            System.out.println("Repository.generateClass() " + e.getLocalizedMessage());
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
                    //  testMsg("checkIdValidity .pas_1", false);

                    valid = false;
                }
            }
            if (name.equals(getTypeName(e))) {
                error("AutoImplement $as should be different than the Interface name ", e);
                //    testMsg("checkIdValidity.pas_2", false);
            }
        } catch (Exception ex) {
            System.out.println("Repository.checkIdValidity() " + ex.getLocalizedMessage());
        }
        // testMsg("valid = " + valid, false);
        return valid;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String getTypeName(Element e)">
    /**
     * Get the simple name of the TypeMirror
     */
    private String getTypeName(Element e) {

        TypeMirror typeMirror = e.asType();
        String[] split = typeMirror.toString().split("\\.");
        try {

        } catch (Exception ex) {
            System.out.println("Repository.getTypeName() " + ex.getLocalizedMessage());
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
        String TAB = "       ";
        if (marco) {
            System.out.println("_________________________________________________________");
        }

        System.out.println(TAB + msg);

        if (marco) {
            System.out.println("___________________________________________________________");
        }
    }

    private void testMsgBlock(String msg, Boolean marco) {
        String TAB = "|=       ";
        if (marco) {

            System.out.println("===============================================================");

        }

        System.out.println(TAB + msg + "     =|");

        if (marco) {
            System.out.println("================================================================");
        }
    }

    /**
     * Esto es para analizar los metodos
     *
     * @param typeElement
     * @return
     */
    private void analizarMetodosQuery(Element element) {
        try {

            if (element.getKind() == ElementKind.METHOD) {
                // only handle methods as targets
                Test.msg("Es un metodo" + element.getSimpleName());
                checkMethod((ExecutableElement) element);
            } else {
                Test.msg("No es un metodo " + element.getSimpleName());
            }

        } catch (Exception e) {
            Test.msg("analizarMetodos() " + e.getLocalizedMessage());
        }
    }

    private void checkMethod(ExecutableElement method) {
        // check for valid name
        String name = method.getSimpleName().toString();
        Test.msg("Metodo -->>" + name);
//        if (!name.startsWith("set")) {
//            printError(method, "setter name must start with \"set\"");
//        } else if (name.length() == 3) {
//            printError(method, "the method name must contain more than just \"set\"");
//        } else if (Character.isLowerCase(name.charAt(3))) {
//            if (method.getParameters().size() != 1) {
//                printError(method, "character following \"set\" must be upper case");
//            }
//        }

        // check, if setter is public
//        if (!method.getModifiers().contains(Modifier.PUBLIC)) {
//            printError(method, "setter must be public");
//        }
//
//        // check, if method is static
//        if (method.getModifiers().contains(Modifier.STATIC)) {
//            printError(method, "setter must not be static");
//        }
    }

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        // get messager for printing errors
        messager = processingEnvironment.getMessager();
    }

    private void printError(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
