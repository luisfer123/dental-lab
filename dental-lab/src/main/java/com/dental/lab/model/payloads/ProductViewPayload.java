package com.dental.lab.model.payloads;

import java.text.DecimalFormat;
import java.util.Base64;

import com.dental.lab.model.entities.Product;

public class ProductViewPayload {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String picture;
	
	private String currentPrice;
	
	public ProductViewPayload() { }

	public ProductViewPayload(Long id, String name, 
			String description, String picture, String currentPrice) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.picture = picture;
		this.currentPrice = currentPrice;
	}
	
	public static ProductViewPayload build(Product product) {
		
		String currentPrice = 
				(new DecimalFormat("0.#"))
				.format(product.getCurrentPrice().getPrice());
		
		String productPicture = null;
		if(product.getPicture() != null && product.getPicture().length > 0) {
			productPicture = 
					Base64.getEncoder().encodeToString(product.getPicture());
		} 
		
		return new ProductViewPayload(
				product.getId(),
				product.getName(),
				product.getDescription(),
				productPicture,
				currentPrice);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentPrice == null) ? 0 : currentPrice.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((picture == null) ? 0 : picture.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductViewPayload other = (ProductViewPayload) obj;
		if (currentPrice == null) {
			if (other.currentPrice != null)
				return false;
		} else if (!currentPrice.equals(other.currentPrice))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (picture == null) {
			if (other.picture != null)
				return false;
		} else if (!picture.equals(other.picture))
			return false;
		return true;
	}

}
