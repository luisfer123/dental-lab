package com.dental.lab.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	
	/**
	 * .
	 * @return - The root category. There can be only one root category, so only
	 * one category can have null as parentCategory
	 */
	@Query("select c from ProductCategory c where c.parentCategory = null")
	ProductCategory findRootCategory();
	
	Set<ProductCategory> findByProducts(Product product);
	
	Optional<ProductCategory> findById(Long categoryId);
	
	Optional<ProductCategory> findByName(String name);
	
}
