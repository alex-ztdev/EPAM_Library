package com.my.library.entities;

import com.my.library.dao.constants.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    private Long orderId;
    private Long userId;
    private Long bookId;
    private LocalDateTime orderStartDate;
    private LocalDateTime orderEndDate;
    private LocalDateTime returnDate;

    private OrderStatus orderStatus;
    private boolean onSubscription;

    public Order() {
    }

    public Order(Long orderId, Long userId, Long bookId, LocalDateTime orderStartDate, LocalDateTime orderEndDate, LocalDateTime returnDate, OrderStatus orderStatus, boolean onSubscription) {
        this.orderId = orderId;
        this.userId = userId;
        this.bookId = bookId;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
        this.returnDate = returnDate;
        this.orderStatus = orderStatus;
        this.onSubscription = onSubscription;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(LocalDateTime orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public LocalDateTime getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(LocalDateTime orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isOnSubscription() {
        return onSubscription;
    }

    public void setOnSubscription(boolean onSubscription) {
        this.onSubscription = onSubscription;
    }
}
