package com.dental.lab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.ProductPricing;

public interface ProductPricingRepository extends JpaRepository<ProductPricing, Long> {
	
	List<ProductPricing> findAll();

}
