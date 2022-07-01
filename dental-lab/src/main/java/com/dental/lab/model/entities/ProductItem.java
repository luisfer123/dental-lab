package com.dental.lab.model.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import org.hibernate.annotations.JoinFormula;

/**
 * A {@linkplain ProductItem} instance is created until a customer adds a 
 * {@linkplain Product} to an {@linkplain Order}.<br>
 * By default, {@code ProductItem.price} equals {@code product.currentPrice}, or it 
 * may be left as null, in such case we use {@code product.currentPrice} instead.
 * If for some reason, a special price will be offer to the client, {@code ProductItem.price}
 * may be used to store that different price.
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
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	/**
	 * By default equals {@code product.currentPrice}, or it may be left
	 *  as null, in such case we use {@code product.currentPrice} instead.
	 * If for some reason, a special price will be offer to the client, 
	 * {@code ProductItem.price} may be used to store that different price.
	 */
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "delivery_deadline")
	private Timestamp deliveriDeadline;
	
	@OneToMany(mappedBy = "productItem",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private Set<ProductItemStatus> status;
	
	@ManyToOne
	@JoinFormula(
			"SELECT pis.id " +
			"FROM Product_item_status pis " +
			"WHERE pis.Product_item_id = id " +
			"ORDER BY pis.creation_date DESC " +
			"LIMIT 1")
	private ProductItemStatus currentStatus;
	
	@OneToMany(mappedBy = "productItem",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true)
	private Set<PartialPayment> partialPayments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Timestamp getDeliveriDeadline() {
		return deliveriDeadline;
	}

	public void setDeliveriDeadline(Timestamp deliveriDeadline) {
		this.deliveriDeadline = deliveriDeadline;
	}

	public Set<ProductItemStatus> getStatus() {
		return status;
	}

	public void setStatus(Set<ProductItemStatus> status) {
		this.status = status;
	}

	public ProductItemStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(ProductItemStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof ProductItem))
			return false;
		
		ProductItem other = (ProductItem) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
