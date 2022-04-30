package com.dental.lab.repositories;

import static org.assertj.core.api.Assertions.assertThat;

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
		
		adminTest.addAuthority(new Authority(EAuthority.ROLE_ADMIN));
		adminTest.addAuthority(new Authority(EAuthority.ROLE_CLIENT));
		adminTest.addAuthority(new Authority(EAuthority.ROLE_DENTIST));
		adminTest.addAuthority(new Authority(EAuthority.ROLE_TECHNICIAN));
		adminTest.addAuthority(new Authority(EAuthority.ROLE_USER));
		em.persist(adminTest);
		
		assertThat(authRepo.findUserAuthoritesByUsername(usernameTestAdmin))
				.extracting("authority")
				.containsExactlyInAnyOrder(EAuthority.ROLE_USER,
						EAuthority.ROLE_CLIENT,
						EAuthority.ROLE_TECHNICIAN,
						EAuthority.ROLE_ADMIN,
						EAuthority.ROLE_DENTIST);
		
		User userTest = new User();
		userTest.setUsername(usernameTestUser);
		userTest.setPassword(passwordTestUser);
		
		userTest.addAuthority(new Authority(EAuthority.ROLE_USER));
		em.persist(userTest);
		
		assertThat(authRepo.findUserAuthoritesByUsername(usernameTestUser))
				.extracting("authority")
				.containsExactlyInAnyOrder(EAuthority.ROLE_USER);
		
		
	}

}
