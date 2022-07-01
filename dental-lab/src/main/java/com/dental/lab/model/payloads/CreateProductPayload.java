package com.dental.lab.model.payloads;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateProductPayload {
	
	@NotBlank(message = "Este campo no puede estar vacío.")
	private String name;
	
	@NotNull(message = "Este campo no puede estar vacío.")
	@Min(value = 0, message = "El valor no puede ser negativo")
	private BigDecimal price;
	
	@NotNull(message = "Este campo no puede estar vacío.")
	private Long categoryId;
	
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

