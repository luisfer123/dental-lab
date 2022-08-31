package com.dental.lab.exceptions;

public class ProductItemNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductItemNotFoundException() {
		super();
	}

	public ProductItemNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProductItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductItemNotFoundException(String message) {
		super(message);
	}

	public ProductItemNotFoundException(Throwable cause) {
		super(cause);
	}

}
