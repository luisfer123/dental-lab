package com.dental.lab.services.impl;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.CustomValidationException;
import com.dental.lab.model.payloads.UpdatePasswordPayload;
import com.dental.lab.security.CustomUserDetails;
import com.dental.lab.services.UserService;
import com.dental.lab.services.UserValidationService;
import com.dental.lab.utils.MultiTreeMap;

@Service
public class UserValidationServiceImpl implements UserValidationService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean validateUsername(String username)
			throws CustomValidationException {
		
		MultiTreeMap<String, String> invalidUsername = new MultiTreeMap<>();
		
		if(username == null || username.isEmpty() || username.contains(" ")) {
			invalidUsername.put("username", "- Nombre de usuario no puede ser vacío o contener espacios");
		}
		
		if(username.length() < 3) {
			invalidUsername.put("username", "- Nombre de usuario debe contener al menos 3 caracteres");
		}
		
		if(userService.existsByUsername(username)) {
			invalidUsername.put("username", "- Nombre de usuario no disponible.");
		}
		
		if(!invalidUsername.isEmpty()) {
			throw new CustomValidationException(
					"Username is not valid", invalidUsername);
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateEmail(String email)
			throws CustomValidationException {
		
		MultiTreeMap<String, String> invalidEmail = new MultiTreeMap<>();
		EmailValidator emailValidator = EmailValidator.getInstance();
		
		if(email == null || email.isEmpty() || email.contains(" ")) {
			invalidEmail.put("email", "- Correo electrónico no puede ser vacío o contener espacios");
		}
		
		if(userService.existsByEmail(email)) {
			invalidEmail.put("email", "- Este correo electrónico ya esta registrado.");
		}
		
		if(!emailValidator.isValid(email)) {
			invalidEmail.put("email", "- Dirección de correo electrónico no válida");
		}
		
		if(!invalidEmail.isEmpty()) {
			throw new CustomValidationException(
					"email is not valid", invalidEmail);
		}
		
		return true;
	}
	
	@Override
	public boolean validateUpdatePassword(UpdatePasswordPayload updatePassword) 
					throws CustomValidationException {
		
		MultiTreeMap<String, String> invalidPassword = new MultiTreeMap<>();
		CustomUserDetails principal =
				(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(updatePassword.getConfirmNewPassword() == null 
				|| updatePassword.getCurrentPassword().length() == 0) {
			invalidPassword.put("currentPassword", "- Este campo no puede estar vacío.");
		}
		
		if(updatePassword.getNewPassword() == null 
				|| updatePassword.getNewPassword().length() == 0) {
			invalidPassword.put("newPassword", "- Este campo no puede estar vacío.");
		}
		
		if(updatePassword.getConfirmNewPassword() == null 
				|| updatePassword.getConfirmNewPassword().length() == 0) {
			invalidPassword.put("confirmNewPassword", "- Este campo no puede estar vacío.");
		}
		
		if(!encoder.matches(updatePassword.getCurrentPassword(), principal.getPassword())) {
			invalidPassword.put("currentPassword", "- Contraceña incorrecta.");
		}
		
		if(!updatePassword.getNewPassword().equals(updatePassword.getConfirmNewPassword())) {
			invalidPassword.put("confirmNewPassword", "- Contraceña y confirmar contraceña deben conicidir.");
		}

		if(!invalidPassword.isEmpty()) {
			throw new CustomValidationException(
					"Error(s) updating the password.", invalidPassword);
		}
		
		return true;
	}

}
