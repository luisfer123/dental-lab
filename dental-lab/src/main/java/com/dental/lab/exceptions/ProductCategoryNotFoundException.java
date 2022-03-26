package com.dental.lab.exceptions;

public class ProductCategoryNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8312997547058297765L;
	
	public ProductCategoryNotFoundException(String message) {
		super(message);
	}

}
