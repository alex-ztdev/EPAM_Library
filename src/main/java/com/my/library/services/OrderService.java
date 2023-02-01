package com.my.library.services;

import com.my.library.dao.TransactionManager;

import com.my.library.entities.Book;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;

import java.util.List;

public interface OrderService extends Service<Order> {
    void save(Order order, BookService bookService, TransactionManager transactionManager) throws ServiceException;

    List<Order> findAllUsersOrders(long userId, int start, int offset) throws ServiceException;

    List<Order> findAll(int start, int offset) throws ServiceException;

    int countUsersOrders(long userId) throws ServiceException;

    double countFine(Order order);

    int countTotalOrders() throws ServiceException;

    void returnOrder(long orderId, BookService bookService, UserService userService, TransactionManager transactionManager) throws ServiceException;

}


