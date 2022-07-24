/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.internal;

import com.github.mustachejava.util.DecoratedCollection;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avbravo
 */
public class JSonObjectProcessorAux {
    private static final String COMMENT = "generated by ToJsonProcessor";

    private ZonedDateTime date;
    private String packageName;
    private String sourceClassName;
    private List<Field> fields = new ArrayList<>();

   public JSonObjectProcessorAux(String packageName, String sourceClassName) {
        this.date = ZonedDateTime.now();
        this.packageName = packageName;
        this.sourceClassName = sourceClassName;
    }

    public String getComment() {
        return COMMENT;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public String getSourceClassNameWithPackage() {
        return packageName + "." + sourceClassName;
    }

    public String getTargetClassName() {
        return sourceClassName + "JsonWriter";
    }

    public String getTargetClassNameWithPackage() {
        return packageName + "." + getTargetClassName();
    }

    public DecoratedCollection<Field> getFields() {
        return new DecoratedCollection( fields );
    }

    public void addGetter(String getter) {
        String fieldName = getter.substring(3);
        char firstChar = fieldName.charAt(0);
        fieldName = Character.toLowerCase(firstChar) + fieldName.substring(1);
        fields.add(new Field(fieldName, getter));
    }

    public static class Field {

        private String name;
        private String getter;

        private Field(String name, String getter) {
            this.name = name;
            this.getter = getter;
        }

        public String getName() {
            return name;
        }

        public String getGetter() {
            return getter;
        }
    }
}