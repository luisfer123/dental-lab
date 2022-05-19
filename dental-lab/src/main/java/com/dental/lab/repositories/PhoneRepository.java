package com.dental.lab.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.Phone;
import com.dental.lab.model.entities.User;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
	
	Set<Phone> findByUser(User user);

}
