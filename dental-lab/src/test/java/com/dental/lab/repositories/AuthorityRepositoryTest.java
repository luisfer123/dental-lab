package com.dental.lab.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AuthorityRepositoryTest {
	
	private static final String usernameTestUser = "user_test";
	private static final String passwordTestUser = "password";
	
	private static final String usernameTestAdmin = "admin_test";
	private static final String passwordTestAdmin = "password";
	
	@Autowired
	private TestEntityManager em;
	
	@Autowired
	private AuthorityRepository authRepo;
	
	@Test
	public void shouldFindUserAuthoritiesByUsername() {
		User adminTest = new User();
		adminTest.setUsername(usernameTestAdmin);
		adminTest.setPassword(passwordTestAdmin);
		adminTest.setAuthorities(
				Stream.of(new Authority(EAuthority.ROLE_ADMIN), new Authority(EAuthority.ROLE_CLIENT), new Authority(EAuthority.ROLE_TECHNICIAN), new Authority(EAuthority.ROLE_USER))
				.collect(Collectors.toCollection(HashSet::new)));
		em.persist(adminTest);
		
		assertThat(authRepo.findUserAuthoritesByUsername(usernameTestAdmin))
				.extracting("authority")
				.containsExactlyInAnyOrder(EAuthority.ROLE_USER,
						EAuthority.ROLE_CLIENT,
						EAuthority.ROLE_TECHNICIAN,
						EAuthority.ROLE_ADMIN);
		
		User userTest = new User();
		userTest.setUsername(usernameTestUser);
		userTest.setPassword(passwordTestUser);
		userTest.setAuthorities(
				Stream.of(new Authority(EAuthority.ROLE_USER))
				.collect(Collectors.toCollection(HashSet::new)));
		em.persist(userTest);
		
		assertThat(authRepo.findUserAuthoritesByUsername(usernameTestUser))
				.extracting("authority")
				.containsExactlyInAnyOrder(EAuthority.ROLE_USER);
		
		
	}

}
