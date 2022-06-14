package com.dental.lab.exceptions;

import com.dental.lab.utils.MultiTreeMap;

/**
 * Meant to be thrown when a form field is not valid.<br>
 * Contains a field named {@code errorMessages} of type {@linkplain MultiTreeMap}.<br>
 * {@code errorMessages} map field should contain as {@code key} the name of the field
 * being validated and as {@code value} custom error message.<br>
 * A {@linkplain MultiTreeMap} is used, so multiple error messages can be added for the
 * same field.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
public class CustomValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MultiTreeMap<String, String> errorMessages;
	
	public CustomValidationException(
			String message, MultiTreeMap<String, String> errorMessages) {
		super(message);
		this.errorMessages = errorMessages;
	}
	
	public MultiTreeMap<String, String> getErrorMessages() {
		return this.errorMessages;
	}

}
