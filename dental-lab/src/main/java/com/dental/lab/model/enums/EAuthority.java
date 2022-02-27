package com.dental.lab.model.enums;

import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;

/**
 * Authorities are use as Roles. Each {@linkplain User} may have one or more Authorities
 * (Roles). {@code ROLE_USER} is mandatory and all registered {@linkplain User}s must have
 * it.
 * {@linkplain Authority} class uses this {@code EAuthority} enum to define the User Roles
 * an {@linkplain User} can have.
 * @author Luis Fernando Martinez Oritz
 *
 */
public enum EAuthority {

	ROLE_USER,
	
	ROLE_CLIENT,
	
	ROLE_TECHNICIAN,
	
	ROLE_ADMIN
	
}
