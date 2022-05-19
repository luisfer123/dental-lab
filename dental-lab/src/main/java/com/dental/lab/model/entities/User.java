package com.dental.lab.model.entities;

import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * There are different types of Users:
 * - Dentist
 * - Technician
 * We use the security Roles ({@linkplain Authority}) to identify the
 * type of User.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
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
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "second_name")
	private String secondName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "second_last_name")
	private String secondLastName;
	
	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "User_has_Authority",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authorities = new HashSet<>();
	
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private Set<Address> addresses = new HashSet<>();
	
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private Set<Phone> phones = new HashSet<>();
	
	public User() {}
	
	public User(String username, String password, String email, String firstName,
			String secondName, String lastName, String secondLastName) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.secondName = secondName;
		this.lastName = lastName;
		this.secondLastName = secondLastName;
	}
	
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
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}
	
	public String getfullLastName() {
		return lastName + " " + secondLastName;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", firstName=" + firstName + ", secondName=" + secondName + ", lastName=" + lastName
				+ ", secondLastName=" + secondLastName + ", authorities=" + authorities + "]";
	}

}
