/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmoordb.core.util;

/**
 *
 * @author avbravo
 */
public class JmoordbCoreException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JmoordbCoreException() {
	}

	public JmoordbCoreException(String msg) {
		super(msg);
	}

	public JmoordbCoreException(Throwable t) {
		super(t);
	}

	public JmoordbCoreException(String msg, Throwable t) {
		super(msg, t);
	}

}