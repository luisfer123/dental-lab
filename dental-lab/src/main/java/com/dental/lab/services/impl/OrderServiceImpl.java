package com.dental.lab.services.impl;

import java.sql.Timestamp;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dental.lab.exceptions.ProductNotFoundException;
import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.Product;
import com.dental.lab.model.entities.ProductItem;
import com.dental.lab.model.entities.ProductItemStatus;
import com.dental.lab.model.entities.ProductOrder;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EProductItemStatus;
import com.dental.lab.model.payloads.PreOrderCurrentStatePayload;
import com.dental.lab.model.payloads.PreOrderProductInfo;
import com.dental.lab.repositories.OrderRepository;
import com.dental.lab.repositories.ProductRepository;
import com.dental.lab.repositories.UserRepository;
import com.dental.lab.security.CustomUserDetails;
import com.dental.lab.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	@Transactional
	public ProductOrder createOrder(PreOrderCurrentStatePayload preOrder) 
			throws ProductNotFoundException, UserNotFoundException {
		
		User user = null;
		
		// If no user was selected, add new ProductOrder to the current logged in User
		if(preOrder.getSelectedUserUsername() == null || preOrder.getSelectedUserUsername().length() == 0) {
			// Add new Order to Logged in User.
			CustomUserDetails principal =
					(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user = userRepo.findById(principal.getId())
					.orElseThrow(() -> new UserNotFoundException("User with id " + principal.getId() + " was not found."));
		} else {
			user = userRepo.findByUsername(preOrder.getSelectedUserUsername())
					.orElseThrow(() -> new UserNotFoundException("User with username " + preOrder.getSelectedUserUsername() + " was not found."));
		}
		
		ProductOrder newOrder = new ProductOrder();
		user.addOrder(newOrder);
		
		// Add creationDate to new Order
		newOrder.setCreationDate(new Timestamp(System.currentTimeMillis()));
		
		// Create ProductItem for each PreOrderProductInfo and add it to new Order
		for(PreOrderProductInfo preOrderProduct : preOrder.getPreOrderProducts()) { 
			ProductItem productItem = new ProductItem();
			productItem.setPrice(preOrderProduct.getPrice());
			productItem.addStatus(new ProductItemStatus(
					EProductItemStatus.RECEIVED, new Timestamp(System.currentTimeMillis())));
			productItem.setNote(preOrderProduct.getNote());
			
			// Add the corresponding Product to ProductItem being created
			Product product = productRepo.findById(preOrderProduct.getProductId())
					.orElseThrow(() -> new ProductNotFoundException("Product with id " + preOrderProduct.getProductId() + " was not found."));
			product.addProductItem(productItem);
			
			// Add new ProductItem to new Order
			newOrder.addProductItem(productItem);
		}
		
		return orderRepo.save(newOrder);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id = :userId")
	public Set<ProductOrder> findByUserId(Long userId) {
		return orderRepo.findByUserId(userId);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id = :userId")
	public Page<ProductOrder> findByUserIdWithProductItems(
			Long userId, int pageNum, int pageSize, String sortBy) {
		
		Page<ProductOrder> ordersPage = orderRepo.findByUserIdWithProductItems(
				userId, PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending()));
		
		// Initialize lazy associations needed
		ordersPage.getContent()
			.forEach(order -> order.getProductItems().forEach(pi -> pi.getProduct().getName()));
		
		return ordersPage;
		
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id = :userId")
	public Page<ProductOrder> findByUserIdWithProductItemsWithGivenStatus(
			Long userId, EProductItemStatus itemStatus, int pageNum, int pageSize, String sortBy) {
		
		Page<ProductOrder> ordersPage = orderRepo.findByUserIdWithProductItemsWithGivenStatus(
				userId, itemStatus, PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending()));
		
		// Initialize lazy associations needed
		ordersPage.getContent()
			.forEach(order -> order.getProductItems().forEach(pi -> pi.getProduct().getName()));
		
		return ordersPage;
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHNICIAN')")
	public boolean hasCredentialsCreateOrderSelectUser() {
		return true;
	}

}
