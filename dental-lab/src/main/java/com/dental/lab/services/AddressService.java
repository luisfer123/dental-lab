package com.dental.lab.services;

import java.util.Set;

import com.dental.lab.model.entities.Address;
import com.dental.lab.model.entities.User;

public interface AddressService {
	
	public Set<Address> getByUser(User user);

}
