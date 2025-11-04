package com.walmart.order_tracking.entity;

import jakarta.persistence.*;

//Purpose of OrderItem entity:
//OrderItem represents each individual product line inside an order.
@Entity
@Table(name = "order_items", indexes = {@Index(name = "idx_order_items", columnList = "order_id")
})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many items belong to one order (FK column: order_id in order_items)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_items_order"))
    private Order order;

    @Column(nullable = false, length = 128)
    private String sku;

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public long getUnitPriceCents() {
        return unitPriceCents;
    }

    public void setUnitPriceCents(long unitPriceCents) {
        this.unitPriceCents = unitPriceCents;
    }

    @Column(nullable = false)
    private int qty;

    @Column(nullable = false)
    private long unitPriceCents;
}
