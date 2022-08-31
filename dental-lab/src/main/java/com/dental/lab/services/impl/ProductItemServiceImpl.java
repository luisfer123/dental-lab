package com.dental.lab.services.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.ProductItemNotFoundException;
import com.dental.lab.model.entities.ProductItem;
import com.dental.lab.model.entities.ProductItemStatus;
import com.dental.lab.model.enums.EProductItemStatus;
import com.dental.lab.repositories.ProductItemRepository;
import com.dental.lab.services.ProductItemService;

@Service
public class ProductItemServiceImpl implements ProductItemService {
	
	@Autowired
	private ProductItemRepository itemRepo;
	
	@Override
	@Transactional(readOnly = true)
	public ProductItem findById(Long productItemId) throws ProductItemNotFoundException {
		return itemRepo.findById(productItemId)
				.orElseThrow(() -> new ProductItemNotFoundException("ProductItem with id: " + productItemId + " was not found."));
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHNICIAN')")
	public ProductItem updateProductItemStatus(
			Long productItemId, EProductItemStatus newStatus) 
					throws ProductItemNotFoundException, IllegalArgumentException {
		
		// Find ProductItem with the given Id
		ProductItem productItem = itemRepo.findById(productItemId)
				.orElseThrow(() -> new ProductItemNotFoundException("ProductItem with id: " + productItemId + " was not found."));
		
		// If the given Status was already set in the ProductItem throw an exception
		productItem.getStatus().forEach(itemStatus -> {
			if(itemStatus.getStatus().equals(newStatus)) {
				throw new IllegalArgumentException("Status " + newStatus + " has already been set.");
			}
		});
		
		// Create new ProductItemStatus with the given ItemStatus
		ProductItemStatus newItemStatus = new ProductItemStatus();
		newItemStatus.setCreationDate(new Timestamp(System.currentTimeMillis()));
		newItemStatus.setStatus(newStatus);
		
		productItem.addStatus(newItemStatus);
		
		return itemRepo.save(productItem);
	}

}
