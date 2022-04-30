package com.dental.lab.model.payloads;

import java.util.List;

public class CreateUserFormPayload {
		
	private String username;
	
	private String password;
	
	private String confirmPassword;
	
	private String email;
	
	private String firstName;
	
	private String secondName;
	
	private String lastName;
	
	private String secondLastName;
	
	private List<String> roles;

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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "CreateUserFormPayload [username=" + username + ", password=" + password + ", confirmPassword="
				+ confirmPassword + ", email=" + email + ", firstName=" + firstName + ", secondName=" + secondName
				+ ", lastName=" + lastName + ", secondLastName=" + secondLastName + ", roles=" + roles + "]";
	}
}
