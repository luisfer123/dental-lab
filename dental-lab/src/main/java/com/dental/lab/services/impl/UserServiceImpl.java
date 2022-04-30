package com.dental.lab.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.AuthorityNotFoundException;
import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.CreateUserFormPayload;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.UserRepository;
import com.dental.lab.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthorityRepository authRepo;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public User findByUsernameWithAuthorities(String username) 
			throws UsernameNotFoundException {
		
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " does not exists!"));
		
		Set<Authority> authorities = authRepo.findUserAuthoritesByUsername(username);
		authorities.forEach(authority -> user.addAuthority(authority));
		
		return user;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepo.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<User> findAll(int pageNum, int pageSize, String sortBy) {
		
		Pageable paging = 
				PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
		
		return userRepo.findAll(paging);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public User saveNewUser(CreateUserFormPayload userPayload)
			throws AuthorityNotFoundException {
		
		User newUser = User.buildWithoutAuthorities(userPayload);
		
		Authority userAuth = authRepo.findByAuthority(EAuthority.ROLE_USER)
				.orElseThrow(() -> new AuthorityNotFoundException("Authority ROLE_USER was not foud."));
		
		// In case no roles were passed, we add the USER_ROLE, which is mandatory, and save the new User.
		if(userPayload.getRoles() == null || userPayload.getRoles().isEmpty()) {
			newUser.addAuthority(userAuth);
			return userRepo.save(newUser);
		}
		
		// Add Authorities passed in the pay load, if ROLE_USER is not included here it will be added later
		for(String role: userPayload.getRoles()) {
			Authority auth = authRepo.findByAuthority(EAuthority.valueOf(role))
					.orElseThrow(() -> new AuthorityNotFoundException("The Authority " + role + " was not found."));
			newUser.addAuthority(auth);
		}
		
		// ROLE_USER is mandatory, if it has not been added, we add it here
		if(!newUser.getAuthorities().contains(userAuth)) {
			newUser.addAuthority(userAuth);
		}
		
		return userRepo.save(newUser);
		
	}

}
