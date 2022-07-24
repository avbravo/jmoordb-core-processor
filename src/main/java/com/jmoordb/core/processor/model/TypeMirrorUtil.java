/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.processor.model;

import java.util.List;
import java.util.function.Supplier;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 *
 * @author avbravo
 */
public class TypeMirrorUtil {
     public static TypeMirror mirror(Supplier<Class<?>> classValue) {
    try {
        var ignored = classValue.get();
        throw new IllegalStateException("Expected a MirroredTypeException to be thrown but got " + ignored);
    } catch (MirroredTypeException e) {
        return e.getTypeMirror();
    }
}

public static List<? extends TypeMirror> mirrorAll(Supplier<Class<?>[]> classValues) {
    try {
        var ignored = classValues.get();
        throw new IllegalStateException("Expected a MirroredTypesException to be thrown but got " + ignored);
    } catch (MirroredTypesException e) {
        return e.getTypeMirrors();
    }
}
}
