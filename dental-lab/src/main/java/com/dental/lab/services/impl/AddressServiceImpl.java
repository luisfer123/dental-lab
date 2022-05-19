package com.dental.lab.services.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Address;
import com.dental.lab.model.entities.User;
import com.dental.lab.repositories.AddressRepository;
import com.dental.lab.services.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Override
	@Transactional(readOnly = true)
	public Set<Address> getByUser(User user) {
		return addressRepo.findByUser(user);
	}

}
