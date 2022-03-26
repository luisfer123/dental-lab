package com.dental.lab.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

}
