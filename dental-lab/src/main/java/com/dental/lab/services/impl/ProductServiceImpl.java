package com.dental.lab.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.repositories.ProductCategoryRepository;
import com.dental.lab.repositories.ProductRepository;
import com.dental.lab.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductCategoryRepository categoryRepo;

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return productRepo.findAll();
	}
	
	@Override
	public List<ProductCategory> findRoodCategories() {
		return categoryRepo.findRootCategories();
	}

}
