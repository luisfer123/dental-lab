package com.dental.lab.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Optional<Product> findById(Long productId);
	
	/**
	 * 
	 * @param categoryId - Id of a {@linkplain ProductCategory} object.
	 * @return A {@linkplain Set} of all {@linkplain Project}s which are associated 
	 * with the {@linkplain ProductCategory} entity with {@code Id} equal to
	 * {@code categoryId}.
	 */
	@Query("select p from Product p join p.categories c where c.id = :categoryId")
	Set<Product> findByCategoryId(@Param("categoryId") Long categoryId);
	
	/**
	 * Finds all {@linkplain Project}s which are associated with the {@linkplain ProductCaegory}
	 * entity with {@code Id} equal to {@code categoryId}. And returns a {@linkplain Page} 
	 * of this {@linkplain Product}s according with the specifications provided by the 
	 * {@linkplain Pageable} object.<br>
	 * 
	 * @param categoryId - Id of a {@linkplain ProductCategory} object.
	 * @param pegeable - {@linkplain Pageable} object used by spring data to 
	 * create and retrieve a {@linkplain Page} o {@linkplain Product}s. Size and
	 * number of the page are provided in this object
	 * @return - {@linkplain Page} of {@linkplain Product}.
	 * 
	 */
	@Query("select p from Product p join p.categories c where c.id = :categoryId")
	Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable paging);
	
	boolean existsById(Long id);
	
}
