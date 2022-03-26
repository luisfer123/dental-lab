package com.dental.lab.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.repositories.ProductCategoryRepository;
import com.dental.lab.services.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
	@Autowired
	private ProductCategoryRepository categoryRepo;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ProductCategory findRoodCategory() {
		return categoryRepo.findRootCategory();
	}

	//TODO create custom exception, handle exception or add throws to method signature.
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findSubCategories(Long categoryId) {
		ProductCategory category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new RuntimeException());
		
		return category.getSubCategories()
				.stream().sorted(Comparator.comparing(ProductCategory::getName))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductCategory> findCategoryPath(Long categoryId)
			throws ProductCategoryNotFoundException {
		
		ProductCategory category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ProductCategoryNotFoundException("ProductCategory with id: " + categoryId + " does not exist"));
		
		List<ProductCategory> categoryPath = new ArrayList<>();
		do {
			categoryPath.add(category);
			category = category.getParentCategory();
		} while(category != null);
		
		Collections.reverse(categoryPath);
		
		return categoryPath;
	}

}
