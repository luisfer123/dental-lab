package com.dental.lab.services;

import java.util.List;
import java.util.NoSuchElementException;

import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.payloads.UpdateUserFullNamePayload;

public interface UpdateUserService {

	User updateUsername(Long userId, String username) throws UserNotFoundException;
	
	User updateEmail(Long userId, String email) throws UserNotFoundException;
	
	User updateFullName(Long userId, UpdateUserFullNamePayload updatedName) throws UserNotFoundException;
	
	User updateAuthorities(Long userId, List<String> roles) throws UserNotFoundException, NoSuchElementException;
	
	User updatePassword(Long userId, String newPassword) throws UserNotFoundException;
}
