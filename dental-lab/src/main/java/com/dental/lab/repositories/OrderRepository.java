package com.dental.lab.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dental.lab.model.entities.ProductOrder;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EProductItemStatus;

public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
	
	Optional<ProductOrder> findById(Long orderId);
	
	List<ProductOrder> findByUser(User user);
	
	@Query("select o from ProductOrder o "
			+ "join o.user u "
			+ "where u.id = :userId")
	Set<ProductOrder> findByUserId(@Param("userId") Long userId);
	
	@Query(value = "select o from ProductOrder o "
					+ "join o.user u "
					+ "where u.id = :userId")
	Page<ProductOrder> findByUserIdWithProductItems(@Param("userId") Long userId, Pageable pageable);
	
	@Query(value = "select distinct o from ProductOrder o "
					+ "join o.user u "
					+ "join o.productItems pi "
					+ "join pi.currentStatus cs "
					+ "where u.id = :userId and cs.status = :itemStatus")
	Page<ProductOrder> findByUserIdWithProductItemsWithGivenStatus(
			@Param("userId") Long userId, @Param("itemStatus") EProductItemStatus itemStatus, Pageable pageable);
	
	
	}
