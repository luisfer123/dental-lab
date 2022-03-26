package com.dental.lab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dental.lab.model.entities.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	
	/**
	 * .
	 * @return - The root category. There can be only one root category, so only
	 * one category can have null as parentCategory
	 */
	@Query("select c from ProductCategory c where c.parentCategory = null")
	ProductCategory findRootCategory();
	
}
