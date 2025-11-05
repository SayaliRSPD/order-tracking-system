package com.walmart.order_tracking.dto;

//DTOs are very imp, you cannot expose your repository directly to frontend, data should transfer through objects.
public record CreateOrderItemRequest(String sku, String name, int qty, long unitPriceCents) {}
