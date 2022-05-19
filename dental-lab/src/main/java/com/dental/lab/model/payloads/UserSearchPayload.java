package com.dental.lab.model.payloads;

import javax.validation.constraints.NotBlank;

public class UserSearchPayload {
	
	private String searchBy;
	
	@NotBlank(message = "Este campo no puede estar vacío")
	private String searchKeyword;
	
	
	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}
