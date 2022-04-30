package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Only one {@linkplain ProductCategory} can be assign to a {@linkplain Product}
 * at a time; thought, once one {@linkplain ProductCategory} is assigned, the
 * parent {@linkplain ProductCategory} and the parent of the parent, and so on...
 * are associated with the {@linkplain Product} as well. So once one 
 * {@linkplain ProductCategory} is assigned, so are all its predecessors in the
 * category tree. All {@linkplain ProductCategory}s associated with one 
 * {@linkplain Product} must belong to a single pat in the {@linkplain ProductCategory} 
 * tree. <br>
 * {@linkplain ProductCategor} has a self referencing association to model the
 * Category-SubCategory model.<br>
 * There can be only one root category, that is, only one category can have 
 * {@code ProductCategory.parentCategory} equal to null.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
@Entity
@Table(name = "Product_category")
public class ProductCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany(mappedBy = "categories")
	private Set<Product> products;
	
	@Column(name = "depth")
	private int depth;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private ProductCategory parentCategory;
	
	@OneToMany(mappedBy = "parentCategory", fetch = FetchType.EAGER)
	private Set<ProductCategory> subCategories;

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
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

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public ProductCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(ProductCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public Set<ProductCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<ProductCategory> subCategories) {
		this.subCategories = subCategories;
	}

}
