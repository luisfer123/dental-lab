package com.dental.lab.services;

import java.util.List;

import com.dental.lab.model.entities.ProductCategory;

public interface ProductCategoryService {
	
	/**
	 * 
	 * @return - The {@linkplain ProductCategory} with {@code parentCategory} attribute 
	 * equal to {@code null}. The must be only one {@code ProductCategory} with its
	 * {@code parentCategory} equal to {@code null}.
	 */
	ProductCategory findRoodCategory();
	
	/**
	 * 
	 * @param categoryId
	 * @return - A sorted List containing all sub-categories of the {@linkplain ProductCategory}
	 * with id {@code categoryId}. Sub-categories are sorted by its name.
	 */
	List<ProductCategory> findSubCategories(Long categoryId);
	
	/**
	 * 
	 * @param categoryId
	 * @return - A list with the categories that are between Root {@linkplain ProductCategory}
	 *  and the the {@linkplain ProductCategory} with {@code id} equal to the passed 
	 *  argument {@code categoryId}. Returned List is sorted by the order the {@linkplain ProductCategory}
	 *  Appear in the Category tree.
	 */
	List<ProductCategory> findCategoryPath(Long categoryId);

}
