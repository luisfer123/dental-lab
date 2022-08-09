package com.dental.lab.model.payloads;

import com.dental.lab.model.enums.EProductItemStatus;

/**
 * Meant to be used as a session attribute to store the details of
 * how orders list should be shown. Number of pages,
 * which attribute should be used to sort the resulting page and whether a 
 * {@linkplain ProductItemStatus} should be used to filter the 
 * {@linkplain ProductOrder}s shown in the page.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
public class OrdersListViewPageDetails {
		
	private Integer pageSize;
	
	private String sortBy;
	
	/**
	 *  When null, {@linkplain ProductItems} with any EProductItemStatus are included.
	 *  otherwise, only {@linkplain ProductItem}s with the given {@linkplain EProductItemStatus}
	 *  are used.
	 */
	private EProductItemStatus productItemStatusFilter;

	public OrdersListViewPageDetails() {
		super();
	}

	public OrdersListViewPageDetails(
			Integer pageSize, String sortBy) {
		super();
		this.pageSize = pageSize;
		this.sortBy = sortBy;
		this.productItemStatusFilter = null;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public EProductItemStatus getProductItemStatusFilter() {
		return productItemStatusFilter;
	}

	public void setProductItemStatusFilter(EProductItemStatus productItemStatusFilter) {
		this.productItemStatusFilter = productItemStatusFilter;
	}

}
