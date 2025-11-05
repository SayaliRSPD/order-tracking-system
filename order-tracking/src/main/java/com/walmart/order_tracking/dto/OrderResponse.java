package com.walmart.order_tracking.dto;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        String status,
        long totalCents,
        String currency,
        Instant createdAt,
        Instant updatedAt,
        List<Item> items
) {
    public record Item(Long id, String sku, String name, int qty, long unitPriceCents) {}
}
