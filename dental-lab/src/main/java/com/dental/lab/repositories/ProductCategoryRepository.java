package com.dental.lab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dental.lab.model.entities.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	
	/**
	 * All {@linkplain ProductCategory} which has a null parent is consider as
	 * a root category.
	 * @return
	 */
	@Query("select c from ProductCategory c where c.parentCategory = null")
	List<ProductCategory> findRootCategories();
	
}
