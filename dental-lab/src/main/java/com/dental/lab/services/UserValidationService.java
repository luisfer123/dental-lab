package com.dental.lab.services;

import com.dental.lab.exceptions.CustomValidationException;
import com.dental.lab.model.payloads.UpdatePasswordPayload;
import com.dental.lab.utils.MultiTreeMap;

public interface UserValidationService {
	
	/**
	 * Validates the given username
	 * 
	 * @param username
	 * @return true if the {@code username} is valid.
	 * @throws CustomValidationException If {@code username} is not valid. In case 
	 * {@code username} is not valid, this exception will contain a {@linkplain MultiTreeMap}
	 * object containing the generated error messages with the {@code key "username"}.
	 */
	boolean validateUsername(String username) throws CustomValidationException;
	
	/**
	 * Validates the given email address.
	 * 
	 * @param email
	 * @return true if the {@code email} is valid.
	 * @throws CustomValidationException If {@code email} is not valid. In case 
	 * {@code email} is not valid, this exception will contain a {@linkplain MultiTreeMap}
	 * object containing the generated error messages with the {@code key "email"}. 
	 */
	boolean validateEmail(String email) throws CustomValidationException;
	
	boolean validateUpdatePassword(UpdatePasswordPayload updatePassword) throws CustomValidationException;

}
