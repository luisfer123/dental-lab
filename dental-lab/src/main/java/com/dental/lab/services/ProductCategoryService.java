package com.dental.lab.services;

import java.util.List;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.payloads.ParentChildrenProductCategoryPayload;

public interface ProductCategoryService {
	
	ProductCategory findById(Long categoryId) throws ProductCategoryNotFoundException;
	
	List<ProductCategory> findCategoryChildrenById(Long categoryId) throws ProductCategoryNotFoundException;
	
	ParentChildrenProductCategoryPayload buildParentChildrenCategoryRestResponse(Long categoryId) throws ProductCategoryNotFoundException;
	
	/**
	 * 
	 * @return - The {@linkplain ProductCategory} with {@code parentCategory} attribute 
	 * equal to {@code null}. The must be only one {@code ProductCategory} with its
	 * {@code parentCategory} equal to {@code null}.
	 */
	ProductCategory findRootCategory();
	
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
	
	/**
	 * Creates a new {@linkplain ProductCategory} and saves it in database.<br>
	 * {@code parentCategoryId} is used to set the parent category of the new
	 * {@code ProductCategory}.<br>
	 * If {@code parentCategoryId} is either {@code null} or is not valid, then
	 * {@code RootCategory} is added as parent category of the new category
	 * created.
	 * 
	 * @param parentCategoryId - can be null
	 * @param name
	 * @return
	 */
	ProductCategory createProductCategory(Long parentCategoryId, String name);
	
	/**
	 * Finds category's depth in the category tree.<br>
	 * {@code RootCategory} is defined to have depth equal to zero.
	 * 
	 * @param category
	 * @return
	 */
	int findCategoryDepth(ProductCategory category);
	

}
