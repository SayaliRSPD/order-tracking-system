package com.walmart.order_tracking.dto;

import java.util.List;

public record CreateOrderRequest(Long customerId, String currency, List<CreateOrderItemRequest> items) {}
