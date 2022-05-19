package com.dental.lab.services;

import java.util.Set;

import com.dental.lab.model.entities.Phone;
import com.dental.lab.model.entities.User;

public interface PhoneService {
	
	public Set<Phone> findByUser(User user);

}
