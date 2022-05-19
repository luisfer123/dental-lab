package com.dental.lab.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.Address;
import com.dental.lab.model.entities.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
	Set<Address> findByUser(User user);

}
