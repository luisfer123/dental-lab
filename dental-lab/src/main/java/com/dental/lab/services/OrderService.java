package com.dental.lab.services;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.dental.lab.exceptions.ProductNotFoundException;
import com.dental.lab.model.entities.ProductOrder;
import com.dental.lab.model.enums.EProductItemStatus;
import com.dental.lab.model.payloads.PreOrderCurrentStatePayload;

public interface OrderService {
	
	ProductOrder createOrder(PreOrderCurrentStatePayload preOrder) throws ProductNotFoundException;
	
	Set<ProductOrder> findByUserId(Long userId);
	
	Page<ProductOrder> findByUserIdWithProductItems(Long userId, int pageNum, int pageSize, String sortBy);
	
	Page<ProductOrder> findByUserIdWithProductItemsWithGivenStatus(Long userId, EProductItemStatus itemStatus, int pageNum, int pageSize, String sortBy);

}
