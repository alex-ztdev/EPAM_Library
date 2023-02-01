package com.my.library.dto;

import com.my.library.entities.Order;
import com.my.library.services.BookService;
import com.my.library.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long orderId;
    private Long userId;
    private String userName;
    private String bookTitle;
    private LocalDateTime orderStartDate;
    private LocalDateTime orderEndDate;
    private LocalDateTime returnDate;
    private boolean onSubscription;
    private double fine;

    public OrderDTO(Order order, double fine, String userName, String bookTitle) {
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.orderStartDate = order.getOrderStartDate();
        this.orderEndDate = order.getOrderEndDate();
        this.returnDate = order.getReturnDate();
        this.onSubscription = order.isOnSubscription();
        this.fine = fine;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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

    public boolean isOnSubscription() {
        return onSubscription;
    }

    public void setOnSubscription(boolean onSubscription) {
        this.onSubscription = onSubscription;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}
