package com.dental.lab.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findById(Long id);
	
	List<User> findByLastName(String lastName);
	
	List<User> findAll();
	
	Page<User> findAll(Pageable pageable);

}
