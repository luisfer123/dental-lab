package com.dental.lab.services;

import java.util.List;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;

public interface ProductService {
	
	List<Product> findAll();
	
	List<ProductCategory> findRoodCategories();

}
