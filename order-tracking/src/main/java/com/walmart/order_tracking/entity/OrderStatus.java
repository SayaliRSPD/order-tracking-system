package com.walmart.order_tracking.entity;

/**have created this enum class to store only valid values of order status
in the database so that no one can store an invalid string in it **/
public enum OrderStatus {
    PENDING, CONFIRMED, CANCELLED, SHIPPED, DELIVERED
}
