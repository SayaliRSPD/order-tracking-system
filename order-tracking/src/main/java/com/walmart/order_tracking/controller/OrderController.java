package com.walmart.order_tracking.controller;

import com.walmart.order_tracking.dto.CreateOrderRequest;
import com.walmart.order_tracking.dto.OrderResponse;
import com.walmart.order_tracking.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest req){
        return ResponseEntity.ok(orderService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(orderService.get(id));
    }
}
