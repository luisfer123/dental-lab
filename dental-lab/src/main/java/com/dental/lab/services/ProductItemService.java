package com.dental.lab.services;

import com.dental.lab.exceptions.ProductItemNotFoundException;
import com.dental.lab.model.entities.ProductItem;
import com.dental.lab.model.enums.EProductItemStatus;

public interface ProductItemService {
	
	ProductItem findById(Long productItemId) throws ProductItemNotFoundException;
	
	ProductItem updateProductItemStatus(Long productItemId, EProductItemStatus newStatus) 
					throws ProductItemNotFoundException, IllegalArgumentException;

}
