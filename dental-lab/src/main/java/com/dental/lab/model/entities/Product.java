package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
	
	@OneToMany(
			mappedBy = "product",
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			orphanRemoval = false)
	private Set<ProductItem> productItems;

}
