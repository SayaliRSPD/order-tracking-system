package com.walmart.order_tracking.controller;

import com.walmart.order_tracking.entity.Customer;
import com.walmart.order_tracking.repo.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    public CustomerController(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Customer req){
        Customer c = new Customer();
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        c = customerRepository.save(c);
        return ResponseEntity.ok(c.getId());
    }
}
