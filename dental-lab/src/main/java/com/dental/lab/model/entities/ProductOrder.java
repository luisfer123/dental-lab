package com.dental.lab.model.entities;

import java.sql.Timestamp;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Each {@linkplain ProductOrder} contains a set of {@linkplain ProductItem}s. <br>
 * {@linkplain ProductItem}s are created at the moment a customer selects a
 * {@linkplain Product} to be added in the {@linkplain ProductOrder}. <br> 
 * If a given {@linkplain ProductOrder} is cancelled (deleted), all {@linkplain ProductItem}s 
 * contained in such {@linkplain ProductOrder} are deleted as well. <br>
 * In the application, each {@linkplain User} may be a customer <br>
 * Administrators can create {@linkplain ProductOrder}s for a customer (which is suppose to be
 * the most common use case), but also customers can create its own {@linkplain ProductOrder}s.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
@Entity
@Table(name = "Product_order")
public class ProductOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	
	@OneToMany(
			mappedBy = "order",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.EAGER)
	private Set<ProductItem> productItems = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "User_id")
	private User user;
	
	public void addProductItem(ProductItem item) {
		this.productItems.add(item);
		item.setOrder(this);
	}
	
	public void removeProductItem(ProductItem item) {
		this.productItems.remove(item);
		item.setOrder(null);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<ProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(Set<ProductItem> productItems) {
		this.productItems = productItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof ProductOrder))
			return false;
		
		ProductOrder other = (ProductOrder) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
