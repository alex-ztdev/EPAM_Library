package com.my.library.services.impl;

import com.my.library.dao.OrderDAO;
import com.my.library.dao.TransactionManager;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger();

    private static final int SUBSCRIPTION_DAYS = 30;
    private static final int IN_READING_ROOM_DAYS = 1;

    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public Optional<Order> find(long id) throws ServiceException {
        try {
            return orderDAO.find(id);
        } catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/ error while executing find method ", e);
        }
    }

    @Override
    public List<Order> findAll() throws ServiceException {
        try {
            return orderDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/error while executing findAll method ", e);
        }
    }

    @Override
    public void save(Order order) throws ServiceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(Order order, BookService bookService, TransactionManager transactionManager) throws ServiceException {

        try {
            logger.log(Level.DEBUG, "OrderServiceImpl/save/Transaction started");
            transactionManager.beginTransaction();

            orderDAO.save(order);

            bookService.decreaseBookQuantity(order.getBookId());

            transactionManager.commit();

            logger.log(Level.DEBUG, "OrderServiceImpl/save/Transaction committed successfully");
        } catch (DaoException e) {
            try {
                transactionManager.rollback();
            } catch (DaoException ex) {
                throw new ServiceException("OrderServiceImpl/error while executing save method" + "Rollback method didn't work", ex);
            }
            throw new ServiceException("OrderServiceImpl/error while executing save method ", e);
        } finally {
            transactionManager.endTransaction();
        }
        logger.log(Level.DEBUG, "OrderServiceImpl/saved order:" + order + " successfully");
    }

    @Override

    public boolean update(Order order) throws ServiceException {
        throw new UnsupportedOperationException();
    }
}
