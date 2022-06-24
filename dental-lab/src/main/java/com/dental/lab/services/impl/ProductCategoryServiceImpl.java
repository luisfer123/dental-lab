package com.dental.lab.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.payloads.ParentChildrenProductCategoryPayload;
import com.dental.lab.repositories.ProductCategoryRepository;
import com.dental.lab.services.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
	@Autowired
	private ProductCategoryRepository categoryRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional(readOnly = true)
	public ProductCategory findById(Long categoryId) 
			throws ProductCategoryNotFoundException {
		
		return categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ProductCategoryNotFoundException("ProductCategory with id: " + categoryId + " was not found."));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findCategoryChildrenById(Long categoryId)
			throws ProductCategoryNotFoundException {
		
		ProductCategory category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ProductCategoryNotFoundException("ProductCategory with id: " + categoryId + " was not found."));
		
		return category.getSubCategories()
				.stream()
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public ParentChildrenProductCategoryPayload buildParentChildrenCategoryRestResponse(
			Long categoryId) throws ProductCategoryNotFoundException {
		
		ProductCategory category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ProductCategoryNotFoundException("ProductCategory with id: " + categoryId + " was not found."));
		
		ParentChildrenProductCategoryPayload response = 
				new ParentChildrenProductCategoryPayload();
		
		response.setParentCategory(category);
		response.setChildrenCategories(
				category.getSubCategories()
				.stream()
				.sorted(Comparator.comparing(ProductCategory::getName))
				.collect(Collectors.toList()));
		response.setParentCategoryPath(this.findCategoryPath(category));
		
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ProductCategory findRootCategory() {
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
	@Transactional(readOnly = true)
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
	
	/**
	 * 
	 * @param category - a {@linkplain ProductCategory} instance.
	 * @return - A list with the categories that are between Root {@linkplain ProductCategory}
	 * and the the {@linkplain ProductCategory} passed as parameter. Returned List is sorted 
	 * by the order the {@linkplain ProductCategory} Appear in the Category tree
	 */
	@Transactional(readOnly = true)
	private List<ProductCategory> findCategoryPath(ProductCategory category)  {
						
		List<ProductCategory> categoryPath = new ArrayList<>();
		do {
			categoryPath.add(category);
			category = category.getParentCategory();
		} while(category != null);
		
		Collections.reverse(categoryPath);
		
		return categoryPath;
	}

}
