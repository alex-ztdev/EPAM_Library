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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger();

    private static final int SUBSCRIPTION_DAYS = 30;


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

            var nowTime = LocalDateTime.now();
            order.setOrderStartDate(nowTime);


            order.setOrderEndDate(order.isOnSubscription() ? nowTime.plusDays(SUBSCRIPTION_DAYS) : LocalDateTime.of(nowTime.toLocalDate(), LocalTime.MAX));

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
    public List<Order> findAllUsersOrders(long userId, int start, int offset) throws ServiceException {
        try {
            return orderDAO.findAllUsersOrders(userId, start, offset);
        } catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/error while executing findAllUsersOrders method", e);
        }
    }

    @Override
    public int countUsersOrders(long userId) throws ServiceException {
        try{
            return orderDAO.countUserOrders(userId);
        }catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/error while executing countUsersOrders method", e);
        }
    }

    @Override

    public boolean update(Order order) throws ServiceException {
        throw new UnsupportedOperationException();
    }
}
