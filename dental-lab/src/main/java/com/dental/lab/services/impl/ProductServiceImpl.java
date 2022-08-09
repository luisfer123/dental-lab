package com.dental.lab.services.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.ProductCategoryNotFoundException;
import com.dental.lab.exceptions.ProductNotFoundException;
import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductCategory;
import com.dental.lab.model.entities.ProductPricing;
import com.dental.lab.model.payloads.CreateProductPayload;
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
	@Transactional(readOnly = true)
	public Product findById(Long productId) throws ProductNotFoundException {
		return productRepo.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " was not found"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Product findByIdWithCategories(Long productId) 
			throws ProductNotFoundException {
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + " was not found."));
		
		Set<ProductCategory> categories = categoryRepo.findByProducts(product);
		categories.forEach(category -> product.addProductCategory(category));
		
		return product;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	@Override
	public Set<Product> findByCategoryId(Long categoryId) {
		return productRepo.findByCategoryId(categoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Product> findByCategoryId(
			Long categoryId, int pageNum, int pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
		return productRepo.findByCategoryId(categoryId, paging);
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsById(Long productId) {
		return productRepo.existsById(productId);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Product updateImage(Long productId, byte[] newPicture) 
			throws ProductNotFoundException {
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + " was not found."));
		
		product.setPicture(newPicture);
		
		return productRepo.save(product);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Product changeProductCategory(Long productId, Long categoryId)
			throws ProductNotFoundException, ProductCategoryNotFoundException {
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + " was not found."));
		ProductCategory category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ProductCategoryNotFoundException("Product Category with id: " + categoryId + " was not found."));
		
		/* One given Product can have only one category, but all parent categories of 
		 * the chosen category are added as well
		 */
		product.removeAllProductCategories();
		do {
			product.addProductCategory(category);
			category = category.getParentCategory();
		} while(category != null);
		
		return productRepo.save(product);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Product updateProductInfo(Long productId, String name, 
			String description, double price) throws ProductNotFoundException {
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + " was not found."));
		
		product.setName(name);
		product.setDescription(description);

		ProductPricing productPrice = new ProductPricing();
		productPrice.setCreationDate(new Timestamp(System.currentTimeMillis()));
		productPrice.setPrice(BigDecimal.valueOf(price));
		
		product.addPrice(productPrice);

		return productRepo.save(product);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Product createProduct(
			CreateProductPayload newProductData) throws ProductCategoryNotFoundException {
		
		Product newProduct = new Product();
		
		newProduct.setName(newProductData.getName());
		newProduct.setDescription(newProductData.getDescription());
		
		ProductCategory category = categoryRepo.findById(newProductData.getCategoryId())
				.orElseThrow(() -> new ProductCategoryNotFoundException("Product Category with id: " + newProductData.getCategoryId() + " was not found."));
		newProduct.addProductCategory(category);
		
		ProductPricing price = new ProductPricing();
		price.setCreationDate(new Timestamp(System.currentTimeMillis()));
		price.setPrice(newProductData.getPrice());
		newProduct.addPrice(price);
		
		return productRepo.save(newProduct);
	}

}
