package com.avbravo.jmoordb.core.processor.model;

import com.avbravo.jmoordb.core.annotation.Count;
import com.avbravo.jmoordb.core.annotation.CountRegex;
import com.avbravo.jmoordb.core.annotation.Delete;
import com.avbravo.jmoordb.core.annotation.Mandatory;
import com.avbravo.jmoordb.core.annotation.Ping;
import com.avbravo.jmoordb.core.annotation.Query;
import com.avbravo.jmoordb.core.annotation.QueryJSON;
import com.avbravo.jmoordb.core.annotation.QueryRegex;
import com.avbravo.jmoordb.core.annotation.Save;
import com.avbravo.jmoordb.core.annotation.Update;
import com.avbravo.jmoordb.core.annotation.enumerations.ActivatePagination;
import com.avbravo.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.avbravo.jmoordb.core.annotation.enumerations.TypeOrder;
import com.avbravo.jmoordb.core.util.ConsoleUtil;
import com.avbravo.jmoordb.core.util.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

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

            String methodName = executableElement.getSimpleName().toString();

            ConsoleUtil.redBackground("methodName-->" + methodName);
            if (!haveAnnotationValid(executableElement)) {
                Test.box(" No tiene anotaciones validas para una interface Repository");
               // printError(element, "No posee anotación valida para un Repositoiry");
                /*
                Aqui coloar el messger
                
                */
                messager.printMessage(Diagnostic.Kind.ERROR, " No tiene anotaciones validas para una interface Repository", element);
            }

            /**
             * Verifico si el metodo tiene anotación Query.classs
             */
            Query query = executableElement.getAnnotation(Query.class);
            if (query == null) {
                Test.msg("................Método no tiene anotación  @Query");
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
                Test.msg("................Método no tiene anotación  @QueryJSON");
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
                Test.msg("................Método no tiene anotación  @QueryRegex");
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
                Test.msg("................Método no tiene anotación  @Count");
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
                Test.msg("................Método no tiene anotación  @CountRegex");
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
                Test.msg("................Método no tiene anotación  @Ping");
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
                Test.msg("................Método no tiene anotación  @Save");
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
                Test.msg("................Método no tiene anotación  @Delete");
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
                Test.msg("................Método no tiene anotación  @Update");
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
