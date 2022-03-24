package com.dental.lab.model.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * A {@linkplain ProductItem} instance is created until a customer adds a 
 * {@linkplain Product} to an {@linkplain Order}. 
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
@Entity
@Table(name = "Product_item")
public class ProductItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

}
