package com.my.library.services;

import com.my.library.dao.TransactionManager;

import com.my.library.dao.constants.OrderStatus;
import com.my.library.entities.Order;
import com.my.library.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> find(long id) throws ServiceException;
    void placeOrder(Order order, BookService bookService, TransactionManager transactionManager) throws ServiceException;

    List<Order> findAllUsersOrders(long userId, int start, int offset, OrderStatus... orderStatus) throws ServiceException;


    int countUsersOrders(long userId, OrderStatus... orderStatuses) throws ServiceException;

    double countFine(Order order);

    void returnOrder(long orderId, BookService bookService, UserService userService, TransactionManager transactionManager) throws ServiceException;

    List<Order> findAllByStatus(int start, int offset, OrderStatus... orderStatus) throws ServiceException;

    int countOrdersByStatus(OrderStatus... orderStatus) throws ServiceException;

    boolean acceptOrder(long id) throws ServiceException;

    void declineOrder(long id, BookService bookService, TransactionManager transactionManager) throws ServiceException;

    void cancelOrder(long userId, long orderId, BookService bookService, TransactionManager transactionManager) throws ServiceException;
}


