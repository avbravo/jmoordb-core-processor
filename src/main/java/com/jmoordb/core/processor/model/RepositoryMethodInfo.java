package com.jmoordb.core.processor.model;

import com.jmoordb.core.annotation.repository.Count;
import com.jmoordb.core.annotation.repository.CountRegex;
import com.jmoordb.core.annotation.Delete;
import com.jmoordb.core.annotation.Mandatory;
import com.jmoordb.core.annotation.repository.Ping;
import com.jmoordb.core.annotation.repository.Query;
import com.jmoordb.core.annotation.repository.QueryJSON;
import com.jmoordb.core.annotation.repository.QueryRegex;
import com.jmoordb.core.annotation.repository.Save;
import com.jmoordb.core.annotation.repository.Update;
import com.jmoordb.core.util.ConsoleUtil;
import com.jmoordb.core.util.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import static org.glassfish.jersey.uri.UriComponent.valid;

/**
 * Converts getters to field
 */
public class RepositoryMethodInfo {

    private Messager messager;
    private final LinkedHashMap<String, String> fields;
    private final List<String> mandatoryFields;

    public RepositoryMethodInfo(LinkedHashMap<String, String> fields, List<String> mandatoryFields) {

        this.fields = fields;
        this.mandatoryFields = mandatoryFields;
    }
// <editor-fold defaultstate="collapsed" desc="set/get">

    public LinkedHashMap<String, String> getFields() {
        return fields;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="RepositoryFieldInfo get(Element element)">

    /**
     * Procesa los metodos definidos en la interface
     *
     * @param element
     * @return
     */
    public static RepositoryMethodInfo get(Element element, Messager messager) {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        List<String> mandatoryFields = new ArrayList<>();

        for (ExecutableElement executableElement
                : ElementFilter.methodsIn(element.getEnclosedElements())) {

            if (executableElement.getKind() != ElementKind.METHOD) {
                continue;
            }
/**
 * Obtiene el nombre del metodo
 */
            String methodName = executableElement.getSimpleName().toString();
           Test.msg("    [[ methodName ]] =" + methodName);
            /**
             * Obtengo el valor de retorno
             */
       TypeMirror returnTypeOfMethod =    executableElement.getReturnType();
       Test.msg("       returnType ="+returnTypeOfMethod);
      //
// if (returnType.getKind() == TypeKind.VOID) {
//    messager.printMessage(Diagnostic.Kind.ERROR,"%s can only be used on a method with a return type non void");
//  }else{
//     if (returnType.getKind() == TypeKind.BOOLEAN) {
//    messager.printMessage(Diagnostic.Kind.ERROR,"%s can only be used on a method with a return type non void");
//  }
// }
 
       /**
        * Obtengo los parametros
        * 
        * Ejemplos
        * https://www.tabnine.com/code/java/methods/javax.lang.model.element.ExecutableElement/getReturnType
        */

     List<? extends VariableElement> parameters = executableElement.getParameters();
// String methodReturnType = executableElement.getReturnType().getKind() == TypeKind.VOID ? TypeKind.VOID.toString() :executableElement.getReturnType().toString();
//       
//if (parameters.size() == parameterTypes.length && methodReturnType.equals(returnType)) {
//      if (methodName == null || method.getSimpleName().toString().equals(methodName)) {
//        // At this point, method name, return type and number of
//        // parameters are correct. Now we need to validate the
//        // parameter types.
//        boolean validMethod = true;
if(parameters.size() <= 0){
     Test.msg("   [ NO TIENE PARAMETROS ] ");
}else{
    for (int i = 0; i < parameters.size(); i++) {
          VariableElement param = parameters.get(i);
          Test.msg("   [param.asType() ] "+param.asType().toString());
          Test.msg("   [param.getSimpleName() ] "+param.getSimpleName().toString());
//          if (!param.asType().toString().equals(parameterTypes[i])) {
//            // Parameter type does not match, this is not the
//            // correct method.
//            validMethod = false;
//            break;
//          }
        }
//        if (validMethod) {
//          return method;
//        }
//      }
}
        


/*

*/

            if (!haveAnnotationValid(executableElement)) {
                Test.box(" No tiene anotaciones validas para una interface Repository");
               
                /*
                Aqui coloar el messger
                
                */
                messager.printMessage(Diagnostic.Kind.ERROR, "Methods without declaring valid annotation for a Repository interface", element);
                /**
                 * Agregue el retorno en esta sección
                 */
                 return new RepositoryMethodInfo(fields, mandatoryFields);
            }

            /**
             * Verifico si el metodo tiene anotación Query.classs
             */
            Query query = executableElement.getAnnotation(Query.class);
            if (query == null) {
              //  Test.msg("................Método no tiene anotación  @Query");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @Query ");
                Test.msg("where(): " + query.where());
                Test.msg("activatePagination(): " + query.activatePagination());
                Test.msg("activateSort(): " + query.activateSort());
                Test.msg("_____________________________________________________");
            }
            /**
             * Lee la anotación @QueryJSOM
             */
            QueryJSON queryJSON = executableElement.getAnnotation(QueryJSON.class);
            if (queryJSON == null) {
               // Test.msg("................Método no tiene anotación  @QueryJSON");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @QueryJSON ");
                Test.msg("activatePagination(): " + queryJSON.activatePagination());
                Test.msg("activateSort(): " + queryJSON.activateSort());
                Test.msg("_____________________________________________________");
            }

            /**
             * Lee la anotación @QueryJSOM
             */
            QueryRegex queryRegex = executableElement.getAnnotation(QueryRegex.class);
            if (queryRegex == null) {
              //  Test.msg("................Método no tiene anotación  @QueryRegex");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @QueryRegex ");
                Test.msg("field(): " + queryRegex.field());
                Test.msg("activatePagination(): " + queryRegex.activatePagination());
                Test.msg("queryRegex(): " + queryRegex.caseSensitive());
                Test.msg("typeOrder(): " + queryRegex.typeOrder());
                Test.msg("_____________________________________________________");
            }

            /**
             * Count
             */
            Count count = executableElement.getAnnotation(Count.class);
            if (count == null) {
              //  Test.msg("................Método no tiene anotación  @Count");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @Count ");
                Test.msg("_____________________________________________________");
            }

            /**
             * Lee la anotación CountRegex
             */
            CountRegex countRegex = executableElement.getAnnotation(CountRegex.class);
            if (countRegex == null) {
              //  Test.msg("................Método no tiene anotación  @CountRegex");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @CountRegex ");
                Test.msg("field(): " + countRegex.field());

                Test.msg("queryRegex(): " + countRegex.caseSensitive());

                Test.msg("_____________________________________________________");
            }

            /**
             * Lee la anotacion @Ping
             */
            Ping ping = executableElement.getAnnotation(Ping.class);
            if (ping == null) {
              //  Test.msg("................Método no tiene anotación  @Ping");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @Ping ");
                Test.msg("_____________________________________________________");
            }
            /**
             * Lee la anotacion @Save
             */
            Save save = executableElement.getAnnotation(Save.class);
            if (save == null) {
            //    Test.msg("................Método no tiene anotación  @Save");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @Save ");
                Test.msg("_____________________________________________________");
            }
            /**
             * Lee la anotacion @Delete
             */
            Delete delete = executableElement.getAnnotation(Delete.class);
            if (delete == null) {
              //  Test.msg("................Método no tiene anotación  @Delete");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @Delete ");
                Test.msg("_____________________________________________________");
            }
            /**
             * Lee la anotacion @Update
             */
            Update update = executableElement.getAnnotation(Update.class);
            if (update == null) {
               // Test.msg("................Método no tiene anotación  @Update");
            } else {
                /**
                 * Imprimo el valor de la anotación Query
                 */
                Test.box(" Componente @Update ");
                Test.msg("_____________________________________________________");
            }

            /**
             * Falta aqui verificar cuando un metodo no tiene anotaciones enviar
             * un mensaje de error
             *
             */
            String fieldName = methodToFieldName(methodName);
            if (fieldName == null) {
                continue;
            }
            String returnType = executableElement.getReturnType().toString();
            if ("void".equals(returnType)) {
                continue;
            }
            fields.put(fieldName, returnType);

            if (executableElement.getAnnotation(Mandatory.class) != null) {
                mandatoryFields.add(fieldName);
            }
        }

        return new RepositoryMethodInfo(fields, mandatoryFields);
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String methodToFieldName(String methodName)">
    /**
     *
     * @param methodName
     * @return
     */
    private static String methodToFieldName(String methodName) {
        if (methodName.startsWith("get")) {
            String str = methodName.substring(3);
            if (str.length() == 0) {
                return null;
            } else if (str.length() == 1) {
                return str.toLowerCase();
            } else {
                return Character.toLowerCase(str.charAt(0)) + str.substring(1);
            }
        } else {

        }
        if (methodName.startsWith("find")) {
            String str = methodName.substring(4);
            if (str.length() == 0) {
                return null;
            } else if (str.length() == 1) {
                return str.toLowerCase();
            } else {
                return Character.toLowerCase(str.charAt(0)) + str.substring(1);
            }
        } else {
            if (methodName.startsWith("delete")) {
                String str = methodName.substring(6);
                if (str.length() == 0) {
                    return null;
                } else if (str.length() == 1) {
                    return str.toLowerCase();
                } else {
                    return Character.toLowerCase(str.charAt(0)) + str.substring(1);
                }
            }
        }
        return null;
    }

    // </editor-fold>
    /**
     * Verifica que tenga un anotación valida para el repositorio
     *
     * @param method
     * @return
     */
    private static Boolean haveAnnotationValid(ExecutableElement executableElement) {
        Boolean isValid = Boolean.FALSE;
        try {
            Query query = executableElement.getAnnotation(Query.class);
            QueryJSON queryJSON = executableElement.getAnnotation(QueryJSON.class);
            QueryRegex queryRegex = executableElement.getAnnotation(QueryRegex.class);
            Count count = executableElement.getAnnotation(Count.class);
            CountRegex countRegex = executableElement.getAnnotation(CountRegex.class);
            Ping ping = executableElement.getAnnotation(Ping.class);
            Save save = executableElement.getAnnotation(Save.class);
            Delete delete = executableElement.getAnnotation(Delete.class);
            Update update = executableElement.getAnnotation(Update.class);
            if (query == null && queryJSON == null && queryRegex == null && count == null && countRegex == null && ping == null && save == null && delete == null && update == null) {

            } else {
                return Boolean.TRUE;
            }


        } catch (Exception e) {
            Test.msg(Test.nameOfClassAndMethod() + " error() "+e.getLocalizedMessage());
        }
        return isValid;
    }

  
}
