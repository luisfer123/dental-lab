package com.dental.lab.model.payloads;

import javax.persistence.Persistence;
import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.xml.crypto.Data;

import org.hibernate.type.Type;

import com.dental.lab.model.entities.User;
import com.dental.lab.model.validation.FieldMatch;

@GroupSequence({
	Type.class, 
	Data.class, 
	Persistence.class, 
	EditUserPayload.class})
@FieldMatch(
		groups = Data.class, 
		first = "password", 
		second = "confirmPassword", 
		message = "The Password and Confirm Password fields must match.")
public class EditUserPayload {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	private String confirmPassword;

	@Email
	@NotBlank
	private String email;

	private String firstName;

	private String secondName;

	private String lastName;

	private String secondLastName;
	
	public EditUserPayload() {
		super();
	}

	public EditUserPayload(String username, String password, String email, String firstName,
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
	
	public static EditUserPayload buildFromUser(User user) {
		return new EditUserPayload(
				user.getUsername(), 
				user.getPassword(), 
				user.getEmail(), 
				user.getFirstName(),
				user.getSecondName(), 
				user.getLastName(), 
				user.getSecondLastName());
	}
	
	public User asUser() {
		return new User(username, password, email, firstName, 
				secondName, lastName, secondLastName);
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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

}
