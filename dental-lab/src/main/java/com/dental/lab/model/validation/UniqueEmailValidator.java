package com.dental.lab.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.dental.lab.services.UserService;

public class UniqueEmailValidator
		implements ConstraintValidator<UniqueEmail, String> {
	
	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		if(userService == null)
			return true;
		
		return email != null &&
				!userService.existsByEmail(email);
		
	}

}
