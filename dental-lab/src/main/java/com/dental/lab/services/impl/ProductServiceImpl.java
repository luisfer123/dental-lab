package com.dental.lab.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.model.entities.Product;
import com.dental.lab.repositories.ProductRepository;
import com.dental.lab.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepo;

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return productRepo.findAll();
	}

	@Override
	public Set<Product> findByCategoryId(Long categoryId) {
		return productRepo.findByCategoryId(categoryId);
	}

}
