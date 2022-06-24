package com.dental.lab.services;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.exceptions.ProductNotFoundException;
import com.dental.lab.model.entities.Product;

public interface ProductService {
	
	List<Product> findAll();
		
	/**
	 * 
	 * @param categoryId
	 * @return - A set with all the {@linkplain Product}s with {@code Product.category}
	 * equal to the {@linkplain ProductCategory} with id {@code categoryId}.
	 */
	Set<Product> findByCategoryId(Long categoryId);
	
	Product findById(Long productId) throws ProductNotFoundException;
	
	Product findByIdWithCategories(Long productId) throws ProductNotFoundException;
	
	/**
	 * 
	 * @param categoryId
	 * 
	 * @return - A {@code List} of {@linkplain Product}s with 
	 * {@code Product.category} equal to the {@linkplain ProductCategory} with 
	 * id {@code categoryId}, and containing only elements of the specified page, and 
	 * sorted using the parameter {@code sortBy}.
	 */
	Page<Product> findByCategoryId(Long categoryId, int pageNum, int pageSize, String sortBy);
	
	Product updateImage(Long productId, byte[] newPicture) throws ProductNotFoundException;
	
	Product changeProductCategory(Long productId, Long CagetegoryId) throws ProductNotFoundException, ProductCategoryNotFoundException;
}
