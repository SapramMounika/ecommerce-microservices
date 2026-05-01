package com.ecommerce.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items") // optional but recommended
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Product reference (from product-service)
    @Column(nullable = false)
    private Long productId;

    // Quantity ordered
    @Column(nullable = false)
    private Integer quantity;

    // Price at time of order (snapshot)
    @Column(nullable = false)
    private Double price;

    // Many items belong to one order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // ✅ Default constructor
    public OrderItem() {}

    // ✅ Correct constructor
    public OrderItem(Long productId, Integer quantity, Double price, Order order) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }

    // =====================
    // GETTERS & SETTERS
    // =====================

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}