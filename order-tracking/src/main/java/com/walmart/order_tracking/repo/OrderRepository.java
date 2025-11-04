package com.walmart.order_tracking.repo;

import com.walmart.order_tracking.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
