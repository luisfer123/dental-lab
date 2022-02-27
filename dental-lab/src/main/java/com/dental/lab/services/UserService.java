package com.dental.lab.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dental.lab.model.entities.User;

public interface UserService {
	
	/**
	 * Finds the {@linkplain User} entity with {@code User.username} equal to the 
	 * parameter {@code username}. {@code User.authorities} property is set with
	 * all the corresponding Authority entities. username is supposed to have an 
	 * unique constrain in the database.
	 * 
	 * @param username {@code username} of the {@linkplain User} entity we want to retrieve.
	 * @return User entity with {@code User.username} equal to {@code username} and 
	 * 			{@code User.authorities} property initialized.
	 * @throws UsernameNotFoundException If no {@linkplain User} with the passed {@code username}
	 * 			was found in the database.
	 */
	User findByUsernameWithAuthorities(String username) throws UsernameNotFoundException;
}