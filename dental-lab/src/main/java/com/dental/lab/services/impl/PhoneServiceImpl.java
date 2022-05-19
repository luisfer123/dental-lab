package com.dental.lab.services.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Phone;
import com.dental.lab.model.entities.User;
import com.dental.lab.repositories.PhoneRepository;
import com.dental.lab.services.PhoneService;

@Service
public class PhoneServiceImpl implements PhoneService {
	
	@Autowired
	private PhoneRepository phoneRepo;
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ADMIN') or principal.username == #user.username")
	public Set<Phone> findByUser(User user) {
		return phoneRepo.findByUser(user);
	}

}
