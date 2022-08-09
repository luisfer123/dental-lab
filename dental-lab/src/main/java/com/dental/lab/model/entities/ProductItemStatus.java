package com.dental.lab.model.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.dental.lab.model.enums.EProductItemStatus;

@Entity
@Table(name = "Product_item_status", 
		uniqueConstraints = @UniqueConstraint(columnNames = {"id", "product_item_id"}))
public class ProductItemStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", 
		columnDefinition = "ENUM('RECEIVED', 'ACCEPTED', 'READY', 'DELIVERED')")
	private EProductItemStatus status;
	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Product_item_id")
	private ProductItem productItem;

	public ProductItemStatus() {
		super();
	}

	public ProductItemStatus(EProductItemStatus status, Timestamp creationDate) {
		super();
		this.status = status;
		this.creationDate = creationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EProductItemStatus getStatus() {
		return status;
	}

	public void setStatus(EProductItemStatus status) {
		this.status = status;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public ProductItem getProductItem() {
		return productItem;
	}

	public void setProductItem(ProductItem productItem) {
		this.productItem = productItem;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof ProductItemStatus))
			return false;
		
		ProductItemStatus other = (ProductItemStatus) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
