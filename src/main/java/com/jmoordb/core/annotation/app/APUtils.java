/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.annotation.app;

import java.util.List;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 *
 * @author avbravo
 */
public class APUtils {
  @FunctionalInterface
  public interface GetClassValue {
    void execute() throws MirroredTypeException, MirroredTypesException;
  }

  public static List<? extends TypeMirror> getTypeMirrorFromAnnotationValue(GetClassValue c) {
    try {
      c.execute();
    }
    catch(MirroredTypesException ex) {
      return ex.getTypeMirrors();
    }
    return null;
  }  
  
}
