package com.klapeks.libs.exceptions;

@SuppressWarnings("serial")
public class NoCatchException extends RuntimeException {
	
	public NoCatchException(String message) {
		super(message);
	}
	
}
