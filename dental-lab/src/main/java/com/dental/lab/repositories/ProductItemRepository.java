package com.dental.lab.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.ProductItem;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
	
	Optional<ProductItem> findById(Long productItemId);

}
