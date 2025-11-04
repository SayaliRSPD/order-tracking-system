package com.walmart.order_tracking.repo;

import com.walmart.order_tracking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
