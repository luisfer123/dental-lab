package com.dental.lab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
}
