package com.dental.lab.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.text.similarity.LevenshteinDistance;
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
import com.dental.lab.exceptions.InvalidArgumentException;
import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.CreateUserFormPayload;
import com.dental.lab.model.payloads.EditUserPayload;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.UserRepository;
import com.dental.lab.services.UserService;
import com.dental.lab.utils.HelperMethods;
import com.dental.lab.utils.MultiTreeMap;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthorityRepository authRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional(readOnly = true)
	public User findById(Long id) throws UserNotFoundException {
		return userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " was not found"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) throws UserNotFoundException {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exists"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public User findByEmail(String email) throws UserNotFoundException {
		return userRepo.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User with email " + email + " was not found"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<User> findByLastName(String lastName) {
		return userRepo.findByLastName(lastName);
	}

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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<User> findAll(int pageNum, int pageSize, String sortBy) {
		
		Pageable paging = 
				PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
		
		return userRepo.findAll(paging);
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<User> findByFullLastName(String fullLastName) 
			throws InvalidArgumentException {
		
		String[] lastNames = HelperMethods.splitTwoWordsString(fullLastName);
		
		if(lastNames.length == 1)
			return findByFullLastName(lastNames[0], null);
		
		if(lastNames.length == 2)
			return findByFullLastName(lastNames[0], lastNames[1]);
		
		throw new InvalidArgumentException("Invalid argument");
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<User> findByFullLastName(String firstLastName, String secondLastName) 
			throws InvalidArgumentException {
		
		final boolean hasFirst = firstLastName != null && firstLastName.length() > 0;
		final boolean hasSecond = secondLastName != null && secondLastName.length() > 0;
		
		if(!hasFirst && !hasSecond)
			throw new InvalidArgumentException("At least one last name must not be nither null nor blank");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> root = query.from(User.class);
		
		if(hasFirst && hasSecond) {
			query.select(root).where(cb.and(
					cb.equal(root.get("lastName"), firstLastName),
					cb.equal(root.get("secondLastName"), secondLastName)));
		}
		
		else if(hasFirst) {
			query.select(root).where(cb.equal(root.get("lastName"), firstLastName));
		}
		
		else if(hasFirst) {
			query.select(root).where(cb.equal(root.get("secondLastName"), secondLastName));
		}
		
		return em.createQuery(query).getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public User saveNewUser(CreateUserFormPayload userPayload)
			throws AuthorityNotFoundException {
		
		User newUser = userPayload.asUserWithoutAuthorities();
		
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
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public User saveEditedUser(EditUserPayload userEdited, Long userId)
			throws UserNotFoundException {
		
		User user = userRepo.findById(userId).orElseThrow(
				() -> new UserNotFoundException("User with id: " + userId + " does  not exists!"));
		
		user.setUsername(userEdited.getUsername());
		user.setEmail(userEdited.getEmail());
		user.setPassword(userEdited.getPassword());
		user.setFirstName(userEdited.getFirstName());
		user.setSecondName(userEdited.getSecondName());
		user.setLastName(userEdited.getLastName());
		user.setSecondLastName(userEdited.getSecondLastName());
		
		return userRepo.save(user);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<String> findUsernamesWithClosestLevenshteinDistance(
			String username, int maxDistance) {
		
		LevenshteinDistance distance = new LevenshteinDistance();
		MultiTreeMap<Integer, String> computedDistances = new MultiTreeMap<>();
		List<String> result = new ArrayList<>();
		
		List<User> allUsers = this.findAll();
		
		allUsers.forEach(user -> computedDistances.put( 
					distance.apply(username, user.getUsername()),
					user.getUsername()));
		
		
		while(!computedDistances.isEmpty() && computedDistances.firstKey() <= maxDistance) {
			computedDistances.remove(computedDistances.firstKey())
				.forEach(value -> result.add(value));
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findSimilarUsersByUsername(String username, int maxDistance) {
		
		if(username == null || username.length() == 0)
			return new ArrayList<>();
		
		List<User> result = new ArrayList<>();
		List<User> allUsers = this.findAll();
		LevenshteinDistance distance = new LevenshteinDistance();
		MultiTreeMap<Integer, User> computedDistances = new MultiTreeMap<>();
		
		allUsers.forEach(user -> {
			computedDistances.put(
					distance.apply(username, user.getUsername()),
					user);
		});
		
		while(!computedDistances.isEmpty() && computedDistances.firstKey() <= maxDistance) {
			computedDistances.remove(computedDistances.firstKey())
				.forEach(user -> result.add(user));
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findSimilarUsersByFullLastName(String fullLastName, int maxDistance) {
		
		String[] lastNames = 
				HelperMethods.splitTwoWordsString(fullLastName);
		
		if(lastNames.length == 1)
			return findSimilarUsersByFullLastName(lastNames[0], null, maxDistance);

		if(lastNames.length == 2)
			return findSimilarUsersByFullLastName(lastNames[0], lastNames[1], maxDistance);
		
		return new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findSimilarUsersByFullLastName(
			String firstLastName, String secondLastName, int maxDistance) {
		
		boolean hasFirst = firstLastName != null && firstLastName.length() > 0;
		boolean hasSecond = secondLastName != null && secondLastName.length() > 0;
		
		if(!hasFirst && !hasSecond)
			return new ArrayList<>();
		
		List<User> result = new ArrayList<>();
		List<User> allUsers = this.findAll();
		LevenshteinDistance distance = new LevenshteinDistance();
		
		if(hasFirst && !hasSecond) {
			MultiTreeMap<Integer, User> firstLastNameDistances = new MultiTreeMap<>();
			allUsers.forEach(user -> {
				firstLastNameDistances.put(
						distance.apply(firstLastName, user.getLastName() == null ? "" : user.getfullLastName()), user);
			});
			
			while(!firstLastNameDistances.isEmpty() 
					&& firstLastNameDistances.firstKey() <= maxDistance) {
				firstLastNameDistances.remove(firstLastNameDistances.firstKey())
					.forEach(user -> result.add(user));
			}
		}
		
		if(hasSecond && !hasFirst) {
			MultiTreeMap<Integer, User> secondLastNameDistances = new MultiTreeMap<>();
			allUsers.forEach(user -> {
				secondLastNameDistances.put(
						distance.apply(secondLastName, user.getSecondLastName() == null ? "" : user.getSecondLastName()), user);
			});
			
			while(!secondLastNameDistances.isEmpty() 
					&& secondLastNameDistances.firstKey() <= maxDistance) {
				secondLastNameDistances.remove(secondLastNameDistances.firstKey())
					.forEach(user -> result.add(user));
			}
		}
		
		if(hasFirst && hasSecond) {
			MultiTreeMap<Integer, User> computedDistances = new MultiTreeMap<Integer, User>();
			allUsers.forEach(user -> {
				computedDistances.put(
						distance.apply(firstLastName + " " + secondLastName, user.getfullLastName() == null ? "" : user.getfullLastName()), 
						user);
			});
			
			while(!computedDistances.isEmpty() 
					&& computedDistances.firstKey() <= maxDistance) {
				computedDistances.remove(computedDistances.firstKey())
					.forEach(user -> result.add(user));
			}
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findSimilarUsersByEmail(String email, int maxDistance) {
		if(email == null || email.length() == 0)
			return new ArrayList<>();
		
		List<User> allUsers = this.findAll();
		List<User> result = new ArrayList<>();
		MultiTreeMap<Integer, User> computedDistances = new MultiTreeMap<Integer, User>();
		LevenshteinDistance distance = new LevenshteinDistance();
		
		allUsers.forEach(user -> {
			computedDistances.put(
					distance.apply(email, user.getEmail() == null ? "" : user.getEmail()),
					user);
		});
		
		while(!computedDistances.isEmpty() 
				&& computedDistances.firstKey() <= maxDistance) {
			computedDistances.remove(computedDistances.firstKey())
				.forEach(user -> result.add(user));
		}
		
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}
	
}
