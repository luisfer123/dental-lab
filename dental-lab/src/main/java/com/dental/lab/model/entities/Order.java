package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.CascadeType;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
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

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof Order))
			return false;
		
		Order other = (Order) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
