package com.dental.lab.model.payloads;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.dental.lab.model.entities.User;
import com.dental.lab.model.validation.UniqueEmail;
import com.dental.lab.model.validation.UniqueUsername;

public class CreateUserFormPayload {

	@NotBlank(message = "Este campo no puede esta vacío")
	@UniqueUsername(message = "Nombre de usuario no disponible")
	@Size(min = 3, message = "El nombre de usuario debe contener al menos 3 caracteres")
	private String username;
	
	@NotBlank(message = "Este campo no puede esta vacío")
	private String password;

	@NotBlank(message = "Este campo no puede esta vacío")
	private String confirmPassword;

	@Email(message = "Introduce una dirección de correo electrónico válido")
	@UniqueEmail(message = "Correo electrónico ya fue registrado por otro usuario")
	private String email;

	private String firstName;

	private String secondName;

	private String lastName;

	private String secondLastName;

	private List<String> roles;

	public CreateUserFormPayload() {
		super();
	}

	public User asUserWithoutAuthorities() {
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
