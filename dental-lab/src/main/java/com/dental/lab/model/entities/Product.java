package com.dental.lab.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
@Entity
@Table(name = "Product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "name")
	private String name;
	
	@JsonIgnore
	@Lob
	@Column(name = "picture", columnDefinition = "MEDIUMBLOB")
	private byte[] picture;
	
	@OneToMany(
			mappedBy = "product",
			cascade = CascadeType.ALL,
			orphanRemoval = false)
	private Set<ProductItem> productItems = new HashSet<>();
	
	@OneToMany(
			mappedBy = "product",
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			orphanRemoval = true)
	private Set<ProductPricing> pricing = new HashSet<>();
	
	/**
	 * Stores the most recent price added in {@code Pricing} table.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(
			"(" +
				"SELECT pp.id " +
				"FROM Product_pricing pp " +
				"WHERE pp.product_id = id " +
				"ORDER BY pp.creation_date DESC " +
				"LIMIT 1" +
			")")
	private ProductPricing currentPrice;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name="Product_has_category",
			joinColumns = @JoinColumn(name="Product_id"),
			inverseJoinColumns = @JoinColumn(name="Product_Category_id"))
	private Set<ProductCategory> categories = new HashSet<>();
	
	public Product() {
		super();
	}

	public Product(String name, String description) {
		super();
		this.description = description;
		this.name = name;
	}

	public void addProductCategory(ProductCategory category) {
		categories.add(category);
		category.getProducts().add(this);
	}
	
	public void removeProductCategory(ProductCategory category) {
		categories.remove(category);
		category.getProducts().remove(this);
	}
	
	public void removeAllProductCategories() {
		categories.forEach(category -> category.getProducts().remove(this));
		categories = new HashSet<>();
	}
	
	public void addPrice(ProductPricing price) {
		pricing.add(price);
		price.setProduct(this);
	}
	
	public void removePrice(ProductPricing price) {
		pricing.remove(price);
		price.setProduct(null);
	}
	
	public void addProductItem(ProductItem item) {
		productItems.add(item);
		item.setProduct(this);
	}
	
	public void removeProductItem(ProductItem item) {
		productItems.remove(item);
		item.setProduct(null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(Set<ProductItem> productItems) {
		this.productItems = productItems;
	}

	public Set<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<ProductCategory> categories) {
		this.categories = categories;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	
	public Set<ProductPricing> getPricing() {
		return pricing;
	}

	public void setPricing(Set<ProductPricing> pricing) {
		this.pricing = pricing;
	}

	public ProductPricing getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(ProductPricing currentPrice) {
		this.currentPrice = currentPrice;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof Product))
			return false;
		
		Product other = (Product) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
