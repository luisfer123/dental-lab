package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dental.lab.model.enums.EAuthority;

/**
 * Entity Class used to correspond with {@linkplain GrantedAuthority} in Spring Security.
 * Authorities are thought as User Roles in this app.<br>
 * Each {@linkplain User} can have one or more Authorities (e.i. Roles) assigned.<br>
 * Permissions are granted by allowing {@linkplain User}s only with specific Roles to 
 * execute specific methods (using @Preauthorized and similar annotations).<br>
 * {@code ROLE_USER} is mandatory and all {@linkplain User}s must have it assigned.<br>
 * Since the c-order in the subsets of permissions grated to each Role 
 * (e.i. {@linkplain Authority}) may no be linear, we need to allow an {@linkplain User}
 * to have multiple {@linkplain Authority}s associated. {@code ROLE_ADMIN} should have
 * all permissions possible in the app, so adding {@code ROLE_ADMIN} and other
 * Roles to a same {@linkplain User} is redundant, we still may do it.<br>
 * The Roles (e.i. Authorities) an {@linkplain User} can have are defined by the 
 * {@linkplain EAuthority} enumeration.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
@Entity
@Table(name = "Authority")
public class Authority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "authority")
	@Enumerated(EnumType.STRING)
	private EAuthority authority;
	
	@Column(name = "description")
	private String description;
	
	@ManyToMany(mappedBy = "authorities")
	private Set<User> users;
	
	public Authority() {}
	
	public Authority(EAuthority auth) {
		this.authority = auth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EAuthority getAuthority() {
		return authority;
	}

	public void setAuthority(EAuthority authority) {
		this.authority = authority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
