package com.jmoordb.core.processor.model;

import com.jmoordb.core.annotation.Mandatory;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Converts getters to field
 */
public class RepositoryCRUDFieldInfo {

    private final LinkedHashMap<String, String> fields;
    private final List<String> mandatoryFields;

    public RepositoryCRUDFieldInfo(LinkedHashMap<String, String> fields, List<String> mandatoryFields) {

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

    public static RepositoryCRUDFieldInfo get(Element element) {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        List<String> mandatoryFields = new ArrayList<>();

        for (ExecutableElement executableElement
                : ElementFilter.methodsIn(element.getEnclosedElements())) {

            if (executableElement.getKind() != ElementKind.METHOD) {
                continue;
            }

            String methodName = executableElement.getSimpleName().toString();

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

        return new RepositoryCRUDFieldInfo(fields, mandatoryFields);
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String methodToFieldName(String methodName)">
    /**
     *
     * @param methodName
     * @return
     */
    private static String methodToFieldName(String methodName) {
      

      
        if (methodName.startsWith("find")) {
            String str = methodName.substring(4);
//            if (str.length() == 0) {
//                return null;
//            } else if (str.length() == 1) {
//                return str.toLowerCase();
//            } else {
//                return Character.toLowerCase(str.charAt(0)) + str.substring(1);
                return methodName;
            }else{
            
        }
       
           

        return null;
    }

    // </editor-fold>
}
