package com.my.library.services;

import com.my.library.dao.TransactionManager;

import com.my.library.entities.Order;
import com.my.library.exceptions.ServiceException;

public interface OrderService extends Service<Order> {
    void save(Order entity, BookService bookService, TransactionManager transactionManager) throws ServiceException;
}


