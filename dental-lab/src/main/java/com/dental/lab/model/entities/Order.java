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
 * Each {@linkplain Order} contains a set of {@linkplain ProductItem}s. <br>
 * {@linkplain ProductItem}s are created at the moment a customer selects a
 * {@linkplain Product} to be added in the {@linkplain Order}. <br> 
 * If a given {@linkplain Order} is cancelled (deleted), all {@linkplain ProductItem}s 
 * contained in such {@linkplain Order} are deleted as well. <br>
 * In the application, each {@linkplain User} may be a customer <br>
 * Administrators can create {@linkplain Order}s for a customer (which is suppose to be
 * the most common use case), but also customers can create its own {@linkplain Order}s.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
@Entity
@Table(name = "Order")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@OneToMany(
			mappedBy = "order",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private Set<ProductItem> productItems;

}
