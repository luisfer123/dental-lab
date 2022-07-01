package com.dental.lab.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	Optional<Order> findById(Long orderId);

}
