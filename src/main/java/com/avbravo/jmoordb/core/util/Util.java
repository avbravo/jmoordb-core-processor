/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.jmoordb.core.util;

import java.util.List;
import java.util.function.Supplier;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 *
 * @author avbravo
 */
public class Util {
    // <editor-fold defaultstate="collapsed" desc="nameOfClassAndMethod()">

    public static String nameOfClassAndMethod() {
        final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        final String s = e.getClassName();
        return s.substring(s.lastIndexOf('.') + 1, s.length()) + "." + e.getMethodName();
    }// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="nameOfClass()">

    public static String nameOfClass() {
        final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        final String s = e.getClassName();
        return s.substring(s.lastIndexOf('.') + 1, s.length());
    }    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="nameOfMethod(">
    public static String nameOfMethod() {
        final StackTraceElement e = Thread.currentThread().getStackTrace()[2];
        final String s = e.getClassName();
        return e.getMethodName();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String nameOfFileInPath(String filenamePath)">
    /**
     *
     * @param filenamePath
     * @return el nombre del archivo que esta en un path
     */
    public static String nameOfFileInPath(String filenamePath) {
        String name = "";
        try {
            name = filenamePath.substring(filenamePath.lastIndexOf(System.getProperty("file.separator")) + 1,
                    filenamePath.lastIndexOf('.'));
        } catch (Exception e) {
            System.out.println(nameOfMethod() + " " + e.getLocalizedMessage());
        }
        return name;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="TypeMirror mirror(Supplier<Class<?>> classValue)">
    public static TypeMirror mirror(Supplier<Class<?>> classValue) {
        try {
            var ignored = classValue.get();
            throw new IllegalStateException("Expected a MirroredTypeException to be thrown but got " + ignored);
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        } // </editor-fold>
    }
// <editor-fold defaultstate="collapsed" desc="List<? extends TypeMirror> mirrorAll(Supplier<Class<?>[]> classValues) ">

    public static List<? extends TypeMirror> mirrorAll(Supplier<Class<?>[]> classValues) {
        try {
            var ignored = classValues.get();
            throw new IllegalStateException("Expected a MirroredTypesException to be thrown but got " + ignored);
        } catch (MirroredTypesException e) {
            return e.getTypeMirrors();
        }
    } // </editor-fold>

    public static void error(String classAndMethod, String msg) {
        System.out.println(msg);
    }
}
