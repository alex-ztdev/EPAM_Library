package com.my.library.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    private Long orderId;
    private Long userId;
    private Long bookId;
    private LocalDateTime orderStartDate;
    private LocalDateTime orderEndDate;
    private boolean isReturned;
    private boolean onSubscription;

    public Order() {
    }

    public Order(Long userId, Long bookId, LocalDateTime orderStartDate, LocalDateTime orderEndDate, boolean isReturned, boolean onSubscription) {
        this.userId = userId;
        this.bookId = bookId;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
        this.isReturned = isReturned;
        this.onSubscription = onSubscription;
    }

    public Order(Long orderId, Long userId, Long bookId, LocalDateTime orderStartDate, LocalDateTime orderEndDate, boolean isReturned, boolean onSubscription) {
        this.orderId = orderId;
        this.userId = userId;
        this.bookId = bookId;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
        this.isReturned = isReturned;
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

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public boolean isOnSubscription() {
        return onSubscription;
    }

    public void setOnSubscription(boolean onSubscription) {
        this.onSubscription = onSubscription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (isReturned != order.isReturned) return false;
        if (onSubscription != order.onSubscription) return false;
        if (orderId != null ? !orderId.equals(order.orderId) : order.orderId != null) return false;
        if (userId != null ? !userId.equals(order.userId) : order.userId != null) return false;
        if (bookId != null ? !bookId.equals(order.bookId) : order.bookId != null) return false;
        if (orderStartDate != null ? !orderStartDate.equals(order.orderStartDate) : order.orderStartDate != null)
            return false;
        return orderEndDate != null ? orderEndDate.equals(order.orderEndDate) : order.orderEndDate == null;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (orderStartDate != null ? orderStartDate.hashCode() : 0);
        result = 31 * result + (orderEndDate != null ? orderEndDate.hashCode() : 0);
        result = 31 * result + (isReturned ? 1 : 0);
        result = 31 * result + (onSubscription ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", orderStartDate=" + orderStartDate +
                ", orderEndDate=" + orderEndDate +
                ", isReturned=" + isReturned +
                ", onSubscription=" + onSubscription +
                '}';
    }
}
