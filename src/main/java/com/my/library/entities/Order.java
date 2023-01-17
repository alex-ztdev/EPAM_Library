package com.my.library.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    private Long orderId;
    private User user;
    private Book book;
    private LocalDateTime orderStartDate;
    private LocalDateTime orderEndDate;
    private LocalDateTime actualReturnDate;

    public Order() {
    }


    public Order(User user, Book book, LocalDateTime orderStartDate, LocalDateTime orderEndDate, LocalDateTime actualReturnDate) {
        this.user = user;
        this.book = book;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
        this.actualReturnDate = actualReturnDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!orderId.equals(order.orderId)) return false;
        if (!user.equals(order.user)) return false;
        if (!book.equals(order.book)) return false;
        if (!orderStartDate.equals(order.orderStartDate)) return false;
        if (!orderEndDate.equals(order.orderEndDate)) return false;
        return actualReturnDate.equals(order.actualReturnDate);
    }

    @Override
    public int hashCode() {
        int result = orderId.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + book.hashCode();
        result = 31 * result + orderStartDate.hashCode();
        result = 31 * result + orderEndDate.hashCode();
        result = 31 * result + actualReturnDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", user=" + user +
                ", book=" + book +
                ", orderStartDate=" + orderStartDate +
                ", orderEndDate=" + orderEndDate +
                ", actualReturnDate=" + actualReturnDate +
                '}';
    }
}
