package com.dental.lab.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.UpdateUserFullNamePayload;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.UserRepository;
import com.dental.lab.services.UpdateUserService;

@Service
public class UpdateUserServiceImpl implements UpdateUserService {
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthorityRepository authRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id = :userId")
	public User updateUsername(Long userId, String username)
		throws UserNotFoundException {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found."));
		
		user.setUsername(username);
		
		return userRepo.save(user);

	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id = :userId")
	public User updateEmail(Long userId, String email)
		throws UserNotFoundException {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found."));
		
		user.setUsername(email);
		
		return userRepo.save(user);

	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id = :userId")
	public User updateFullName(Long userId, UpdateUserFullNamePayload updatedName)
			throws UserNotFoundException {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found."));
		
		user.setFirstName(updatedName.getFirstName());
		user.setSecondName(updatedName.getSecondName());
		user.setLastName(updatedName.getLastName());
		user.setSecondLastName(updatedName.getSecondLastName());
		
		return userRepo.save(user);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public User updateAuthorities(Long userId, List<String> roles)
			throws UserNotFoundException, NoSuchElementException {
		
		Set<Authority> authorities = new HashSet<>();
		
		// If no role is passed, ROLE_USER is added by default
		authorities.add(authRepo.findByAuthority(EAuthority.ROLE_USER).get());
		
		if(roles != null && !roles.isEmpty()) {
			roles.forEach(role -> {
				Authority auth = 
						authRepo.findByAuthority(EAuthority.valueOf(role))
						.get();
				authorities.add(auth);
			});
		}
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found."));
		
		// Remove all current roles User has
		user.removeAllAuthorities();
		// Add the new authorities
		authorities.forEach(auth -> user.addAuthority(auth));
		
		return userRepo.save(user);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id = :userId")
	public User updatePassword(Long userId, String newPassword)
			throws UserNotFoundException {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found."));
		
		user.setPassword(encoder.encode(newPassword));
		
		return userRepo.save(user);
	}

}
