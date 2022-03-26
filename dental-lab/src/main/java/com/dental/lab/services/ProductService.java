package com.dental.lab.services;

import java.util.List;
import java.util.Set;

import com.dental.lab.model.entities.Product;

public interface ProductService {
	
	List<Product> findAll();
		
	Set<Product> findByCategoryId(Long categoryId);
}
