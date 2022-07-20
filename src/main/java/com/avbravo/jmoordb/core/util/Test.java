/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.jmoordb.core.util;

import com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.EIGTH;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.FIVE;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.FOUR;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.ONE;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.SEVEN;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.SIX;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.THREE;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.TWO;
import static com.avbravo.jmoordb.core.annotation.enumerations.LookupSupplierLevel.ZERO;

/**
 *
 * @author avbravo
 */
public class Test {

    public static void msg(String message) {
        System.out.println("    [ " + message + "]");
    }

    public static void msgTab(String message) {
        System.out.println("          [ " + message + "]");
    }

    public static void warning(String message) {
        System.out.println(".....................Advertencia.............................");
        System.out.println("");
        System.out.println("          [ " + message + "]");
        System.out.println("");
        System.out.println("..............................................................");
    }

    public static void box(String message) {
        System.out.println("_______________________________________");
        System.out.println("[ " + message + "]");
        System.out.println("_______________________________________");
    }

    public static void error(String message) {
        System.out.println(".....................Error.............................");
        System.out.println("");
        System.out.println("          [ " + message + "]");
        System.out.println("");
        System.out.println("..............................................................");
    }

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

    // <editor-fold defaultstate="collapsed" desc="LookupSupplierLevel decrement(LookupSupplierLevel level) ">
    /**
     *
     * @param level
     * @return LookupSupplierLever decrementado
     */
    public static LookupSupplierLevel decrement(LookupSupplierLevel level) {
        LookupSupplierLevel result = level;
        try {

            
            switch (level) {
                case ZERO:
                    warning("Nivel minimo " + LookupSupplierLevel.ZERO);
                    result = LookupSupplierLevel.ZERO;
                    break;
                case ONE:
                    result = LookupSupplierLevel.ZERO;
                    break;
                case TWO:
                    result = LookupSupplierLevel.ONE;
                    break;
                case THREE:
                    result = LookupSupplierLevel.TWO;
                    break;
                case FOUR:
                    result = LookupSupplierLevel.THREE;
                    break;
                case FIVE:
                    result = LookupSupplierLevel.FOUR;
                    break;
                case SIX:
                    result = LookupSupplierLevel.FIVE;
                    break;
                case SEVEN:
                    result = LookupSupplierLevel.SIX;
                    break;
                case EIGTH:
                    result = LookupSupplierLevel.SEVEN;                   
                    break;

            }
        } catch (Exception e) {
            error(nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="LookupSupplierLevel decrement(LookupSupplierLevel level) ">

    /**
     *
     * @param level
     * @return LookupSupplierLever decrementado
     */
    public static LookupSupplierLevel increment(LookupSupplierLevel level) {
        LookupSupplierLevel result = level;
        try {
            switch (level) {
                case ZERO:
                    result = LookupSupplierLevel.ONE;
                    break;
                case ONE:
                    result = LookupSupplierLevel.TWO;
                    break;
                case TWO:
                    result = LookupSupplierLevel.THREE;
                    break;
                case THREE:
                    result = LookupSupplierLevel.FOUR;
                    break;
                case FOUR:
                    result = LookupSupplierLevel.FIVE;
                    break;
                case FIVE:
                    result = LookupSupplierLevel.SIX;
                    break;
                case SIX:
                    result = LookupSupplierLevel.SEVEN;
                    break;
                case SEVEN:
                    result = LookupSupplierLevel.EIGTH;
                    break;
                case EIGTH:
                    result = LookupSupplierLevel.EIGTH;
                    warning("Nivel maximo es " + LookupSupplierLevel.EIGTH);
                    break;

            }

        } catch (Exception e) {
            error(nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Integer toInteger(LookupSupplierLevel level) ">

   /**
    * 
    * @param level
    * @return LookupSupplierLevel convertido a enteror
    */
    public static Integer toInteger(LookupSupplierLevel level) {
        Integer result = 0;
        try {
            switch (level) {
                case ZERO:
                    result = 0;
                    break;
                case ONE:
                    result = 1;
                    break;
                case TWO:
                    result = 2;
                    break;
                case THREE:
                    result = 3;
                    break;
                case FOUR:
                    result = 4;
                    break;
                case FIVE:
                    result = 5;
                    break;
                case SIX:
                    result = 6;
                    break;
                case SEVEN:
                    result = 7;
                    break;
                case EIGTH:
                    result = 8;
                    break;

            }

        } catch (Exception e) {
            error(nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="LookupSupplierLevel toLookupSupplierLevel(Integer value) ">

   /**
    * 
    * @param value
    * @return convierte un numero en LookupSupplierLevel
    */
    public static LookupSupplierLevel toLookupSupplierLevel(Integer value) {
       LookupSupplierLevel result = LookupSupplierLevel.ZERO;
        try {
            switch (value) {
                case 0:
                 result = LookupSupplierLevel.ZERO;
                    break;
                case 1:
           result = LookupSupplierLevel.ONE;
                    break;
                case 2:
                     result = LookupSupplierLevel.TWO;
                    break;
                case 3:
                     result = LookupSupplierLevel.THREE;
                    break;
                case 4:
                    result =    result = LookupSupplierLevel.FOUR;
                    break;
                case 5:
                    result =    result = LookupSupplierLevel.FIVE;
                    break;
                case 6:
                    result =    result = LookupSupplierLevel.SIX;
                    break;
                case 7:
                    result =    result = LookupSupplierLevel.SEVEN;
                    break;
                case 8:
                    result =    result = LookupSupplierLevel.EIGTH;
                    break;

            }

        } catch (Exception e) {
            error(nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Integer diference(LookupSupplierLevel level, LookupSupplierLevel levelLocal)">

  /**
   * 
   * @param level
   * @param levelLocal
   * @return  diferencia entre level y levelLocal
   */
    public static Integer diference(LookupSupplierLevel level, LookupSupplierLevel levelLocal) {
     Integer result =0;
        try {            
            result = toInteger(level) - toInteger(levelLocal);
            if(result < 0){
                result =0;
            }
            
        } catch (Exception e) {
            error(nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return result;
    }
// </editor-fold>
    
    
    

}
