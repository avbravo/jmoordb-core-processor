/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jmoordb.core.annotation.repository;

import com.jmoordb.core.annotation.enumerations.ActivatePagination;
import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.annotation.enumerations.TypeOrder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author avbravo
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface QueryRegex {

    String field();

    CaseSensitive caseSensitive() default CaseSensitive.NO;

    TypeOrder typeOrder() default TypeOrder.ASC;

    ActivatePagination activatePagination() default ActivatePagination.ON;
}
