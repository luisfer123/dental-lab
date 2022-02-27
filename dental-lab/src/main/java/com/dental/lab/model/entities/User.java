package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "User")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column
	private String email;
	
	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "User_has_Authority",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authorities;
	
	public void addAuthority(Authority authority) {
		authorities.add(authority);
		authority.getUsers().add(this);
	}
	
	public void removeAuthority(Authority authority) {
		authorities.remove(authority);
		authority.getUsers().remove(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if(!(o instanceof User))
			return false;
		
		User other = (User) o;
		
		return id != null &&
				id.equals(other.getId());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
