package com.walmart.order_tracking.repo;

import com.walmart.order_tracking.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
