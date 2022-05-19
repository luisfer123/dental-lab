package com.dental.lab.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.repositories.AuthorityRepository;
import com.dental.lab.repositories.UserRepository;
import com.dental.lab.services.impl.UserServiceImpl;

@Transactional
@ExtendWith(value = { SpringExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserServiceUnitTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepo;

	@Mock
	private AuthorityRepository authRepo;

	private static final String usernameTestUser1 = "Test_user_1";
	private static final String passwordTestUser1 = "password";
	private static final String emailTestUser1 = "testuser1@mail.com";

	@BeforeEach
	public void setup() {

		User user = new User();
		user.setUsername(usernameTestUser1);
		user.setPassword(passwordTestUser1);
		user.setEmail(emailTestUser1);
		
		Set<Authority> authorities = new HashSet<>();

		Mockito.when(userRepo.findByUsername(user.getUsername()))
				.thenReturn(Optional.of(user));

		Mockito.when(authRepo.findUserAuthoritesByUsername(user.getUsername()))
				.thenReturn(authorities);

	}

	@Test
	public void findByUsernameWithAuthoritiesTest() {
		User user = userService.findByUsernameWithAuthorities(usernameTestUser1);

		assertNotNull(user);
		assertEquals(usernameTestUser1, user.getUsername());
	}
	
	@Test
	public void findByFullLastNameTest() {
		
	}
	
	@Test
	public void findUsernamesWithClosestLevenshteinDistanceTest() {
		
		User user = new User();
		user.setUsername("user");
		user.setPassword("password");
		user.setEmail("user@mail.com");
		
		User user1 = new User();
		user1.setUsername("user1");
		user1.setPassword("password");
		user1.setEmail("user1@mail.com");
		
		User user2 = new User();
		user2.setUsername("user2");
		user2.setPassword("password");
		user2.setEmail("user2@mail.com");
		
		User otherUser = new User();
		otherUser.setUsername("otherUser");
		otherUser.setPassword("password");
		otherUser.setEmail("otherUser@mail.com");
		
		
		List<User> allUsers = Arrays.asList(user, user1, user2, otherUser);
		
		Mockito.when(userRepo.findAll())
			.thenReturn(allUsers);
		
		List<String> result = 
				userService.findUsernamesWithClosestLevenshteinDistance("wser", 2);
		
		assertThat(result)
			.containsOnly("user1", "user2", "user");
	}

}
