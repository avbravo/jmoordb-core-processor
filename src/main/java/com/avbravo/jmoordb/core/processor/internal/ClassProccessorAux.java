package com.avbravo.jmoordb.core.processor.internal;

import com.avbravo.jmoordb.core.annotation.Repository;
import com.avbravo.jmoordb.core.util.Test;
import java.util.*;

/**
 * This class only works if we add elements in proper sequence.
 */
public class ClassProccessorAux {

    public static final String LINE_BREAK = System.getProperty("line.separator");
    public static String TAB="   ";
         
    private StringBuilder builder = new StringBuilder();
    private String className;
    private Map<String, String> fields = new LinkedHashMap<>();

    public ClassProccessorAux() {

    }

    // <editor-fold defaultstate="collapsed" desc="JClass definePackage(String packageName)">
/**
 * 
 * @param packageName
 * @return inserta en package en la clase
 */
    public ClassProccessorAux definePackage(String packageName) {
        if (packageName != null) {
            builder.append("package ")
                    .append(packageName)
                    .append(";")
                    .append(LINE_BREAK);
        }
        return this;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="JClass addImport(String importPackage)">
/** 
 * 
 * @param importPackage
 * @return 
 */
    public ClassProccessorAux addImport(String importPackage) {
        builder.append("import ")
                .append(importPackage)
                .append(";")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="JClass addAnnotations(String annotation)">
/**
 * 
 * @param annotation
 * @return agrega anotaciones
 */
    public ClassProccessorAux addAnnotations(String annotation) {
        builder.append(annotation)
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="JClass addInject(String injectSentence) ">
/**
 * 
 * @param injectSentence
 * @return 
 */
    public ClassProccessorAux addInject(String injectSentence) {
        builder.append(TAB+"@Inject ")
                .append(LINE_BREAK)
                .append(TAB+injectSentence)
                .append(";")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="JClass addComment(String comment) ">
/**
 * 
 * @param comment
 * @return inserta comentarios
 */
    public ClassProccessorAux addComment(String comment) {
        builder.append("/*")
                .append(LINE_BREAK)
                .append(TAB+comment)
                .append(LINE_BREAK)
                .append("*/")                
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="JClass addEditorFoldStart(String desc) ">
/**
 * 
 * @param desc. -Utiloce \" si necesita incluir " en el texto
 * @return inserta un editor fold que sirve como ayuda a NetBeans IDE
 */
    public ClassProccessorAux addEditorFoldStart(String desc) {
        builder.append("// <editor-fold defaultstate=\"collapsed\" desc=\"")
                .append(desc)
                .append("\">")                
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="JClass addEditorFoldClose() ">
/**
 * 
 * @param desc
 * @return cierra un editor fold que sirve como ayuda a NetBeans IDE
 */
    public ClassProccessorAux addEditorFoldEnd() {
        builder.append("// </editor-fold>")                    
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="JClass addInjectConfigProperties(String injectSentence)">
/**
 * Injecta config properties
 * @param injectSentence
 * @return 
 */
    public ClassProccessorAux addInjectConfigProperties(String  nameConfigProperty, String javatype, String javaNameVariable) {
        builder.append(TAB+"@Inject ")
                .append(LINE_BREAK)
                .append(TAB+"@ConfigProperty(name = \""+nameConfigProperty +"\")")
                .append(LINE_BREAK)
                .append(TAB+"private "+javatype + " "+javaNameVariable)
                .append(";")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="JClass defineClass(String startPart, String name, String extendPart)">
/**
 * 
 * @param startPart
 * @param name
 * @param extendPart
 * @return 
 */
    
    public ClassProccessorAux defineClass(String startPart, String name, String extendPart) {
        className = name;
        builder.append(LINE_BREAK).append(LINE_BREAK)
                .append(startPart)
                .append(" ")
                .append(name);
        if (extendPart != null) {
            builder.append(" ")
                    .append(extendPart);
        }

        builder.append(" {")
                .append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="JClass addFields(LinkedHashMap<String, String> identifierToTypeMap)">
/**
 * 
 * @param identifierToTypeMap
 * @return 
 */
    public ClassProccessorAux addFields(LinkedHashMap<String, String> identifierToTypeMap) {
        for (Map.Entry<String, String> entry : identifierToTypeMap.entrySet()) {
            addField(entry.getValue(), entry.getKey());
        }
        return this;
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="JClass addField(String type, String identifier)">

    public ClassProccessorAux addField(String type, String identifier) {
        fields.put(identifier, type);
        builder.append("private ")
                .append(type)
                .append(" ")
                .append(identifier)
                .append(";")
                .append(LINE_BREAK);

        return this;
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="JClass addConstructor(String accessModifier, List<String> fieldsToBind) ">
/**
 * 
 * @param accessModifier
 * @param fieldsToBind
 * @return 
 */
    public ClassProccessorAux addConstructor(String accessModifier, List<String> fieldsToBind) {
        builder.append(LINE_BREAK)
                .append(accessModifier)
                .append(" ")
                .append(className)
                .append("(");

        for (int i = 0; i < fieldsToBind.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            String name = fieldsToBind.get(i);
            builder.append(fields.get(name))
                    .append(" ")
                    .append(name);
        }
        builder.append(") {");
        for (int i = 0; i < fieldsToBind.size(); i++) {
            builder.append(LINE_BREAK);

            String name = fieldsToBind.get(i);
            builder.append("this.")
                    .append(name)
                    .append(" = ")
                    .append(name)
                    .append(";");
        }
        builder.append(LINE_BREAK);
        builder.append("}");
        builder.append(LINE_BREAK);

        return this;

    }
// </editor-fold>
    public ClassProccessorAux addConstructor(String accessModifier, boolean bindFields) {
        addConstructor(accessModifier,
                bindFields ? new ArrayList(fields.keySet())
                        : new ArrayList<>());
        return this;
    }

    public ClassProccessorAux addMethod(MethodProcessorAux method) {
        builder.append(LINE_BREAK)
                .append(method.end())
                .append(LINE_BREAK);
        return this;
    }

    // <editor-fold defaultstate="collapsed" desc="ClassProccessorAux addNestedClass(ClassProccessorAux jClass)">

    public ClassProccessorAux addNestedClass(ClassProccessorAux jClass) {
        builder.append(LINE_BREAK);
        builder.append(jClass.end());
        builder.append(LINE_BREAK);
        return this;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ClassProccessorAux createSetterForField(String name)">

    public ClassProccessorAux createSetterForField(String name) {
        if (!fields.containsKey(name)) {
            throw new IllegalArgumentException("Field not found for setter: " + name);
        }
        addMethod(new MethodProcessorAux()
                .defineSignature("public", false, "void")
                .name("set" + Character.toUpperCase(name.charAt(0)) + name.substring(1))
                .defineBody(" this." + name + " = " + name + ";"));
        return this;
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="ClassProccessorAux createGetterForField(String name)">
   
    public ClassProccessorAux createGetterForField(String name) {
        if (!fields.containsKey(name)) {
            throw new IllegalArgumentException("Field not found for Getter: " + name);
        }
        addMethod(new MethodProcessorAux()
                .defineSignature("public", false, fields.get(name))
                .name("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1))
                .defineBody(" return this." + name + ";"));
        return this;
    }
    // </editor-fold> 
// <editor-fold defaultstate="collapsed" desc="end()">

    /**
     *
     * @return
     */
    public String end() {
        builder.append(LINE_BREAK + "}");
        return builder.toString();

    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ClassProccessorAux generateImport(ClassProccessorAux classProccessorAux ,Repository repository)">
    
    /**
     * 
     * @param classProccessorAux
     * @param repository
     * @return  Genera los imports para el Repository
     */
    public ClassProccessorAux generateImport(Repository repository){
        
        try {
             if (!repository.jakarta()) {
                /*
            Java EE
                 */
                addImport("javax.enterprise.context.ApplicationScoped");
                addImport("javax.inject.Inject");
                addImport("javax.json.bind.Jsonb");
                addImport("javax.json.bind.JsonbBuilder");
            } else {
                /**
                 * Jakarta EE
                 */
                addImport("jakarta.enterprise.context.ApplicationScoped");
                addImport("jakarta.inject.Inject");
                addImport("jakarta.json.bind.Jsonb");
                addImport("jakarta.json.bind.JsonbBuilder");

            }
            /**
             * Microprofile
             */
            addImport("org.eclipse.microprofile.config.Config");
            addImport("org.eclipse.microprofile.config.inject.ConfigProperty");
            /**
             * MongoDB
             */
            addImport("com.mongodb.client.MongoDatabase;");
            addImport("static com.mongodb.client.model.Filters.eq");
            addImport("com.mongodb.client.MongoClient");
            addImport("com.mongodb.client.MongoCollection");
            addImport("com.mongodb.client.MongoCursor");

            addImport("java.util.ArrayList");
            addImport("java.util.List");
            addImport("java.util.Optional");
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod()+  " "+e.getLocalizedMessage());
        }
        return this;
    }
    
// </editor-fold>
    
}
