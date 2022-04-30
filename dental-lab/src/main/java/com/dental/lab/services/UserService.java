package com.dental.lab.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dental.lab.exceptions.AuthorityNotFoundException;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.payloads.CreateUserFormPayload;

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
	
	List<User> findAll();
	
	Page<User> findAll(int pageNum, int pageSize, String sortBy);
	
	/**
	 * Creates a new {@linkplain User} from the {@linkplain CreateUserFormPayload}
	 * parameter passed, adds the corresponding {@linkplain Authority}s and saves it
	 * to the database.<br>
	 * If {@code CreateuserFormPayload.roles} is empty, or it does not contain
	 * {@code ROLE_USER}, then the {@code EAuthority.ROLE_USER} is added. All registered 
	 * User must contain at least the {@code EAuthority.ROLE_USER} Authority.
	 * 
	 * @param userPayload contains the User's information to create the new {@linkplain User}
	 * to be saved.
	 * @return The saved {@linkplain User}
	 * @throws AuthorityNotFoundException If any element in the 
	 * {@code CreateuserFormPayload.roles} list does not correspond with some
	 * {@linkplain EAuthority} constant.
	 */
	User saveNewUser(CreateUserFormPayload userPayload) throws AuthorityNotFoundException;
}