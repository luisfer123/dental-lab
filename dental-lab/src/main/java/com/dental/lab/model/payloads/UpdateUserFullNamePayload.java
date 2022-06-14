package com.dental.lab.model.payloads;

public class UpdateUserFullNamePayload {
	
	private String firstName;
	
	private String secondName;
	
	private String lastName;
	
	private String secondLastName;

	public UpdateUserFullNamePayload() {
		super();
	}

	public UpdateUserFullNamePayload(String firstName, String secondName, String lastName, String secondLastName) {
		super();
		this.firstName = firstName;
		this.secondName = secondName;
		this.lastName = lastName;
		this.secondLastName = secondLastName;
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

	@Override
	public String toString() {
		return "UpdateUserFullNamePayload [firstName=" + firstName + ", secondName=" + secondName + ", lastName="
				+ lastName + ", secondLastName=" + secondLastName + "]";
	}

}
