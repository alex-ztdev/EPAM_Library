package com.my.library.services;

import com.my.library.dao.TransactionManager;

import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;

import java.util.List;

public interface OrderService extends Service<Order> {
    void save(Order order, BookService bookService, TransactionManager transactionManager) throws ServiceException;

    List<Order> findAllUsersOrders(long userId, int start, int offset) throws ServiceException;

    int countUsersOrders(long userId) throws ServiceException;

    double countFine(Order order);
}


