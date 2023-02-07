package com.my.library.services.impl;

import com.my.library.dao.OrderDAO;
import com.my.library.dao.TransactionManager;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    //TODO: Check if user is banned! Before returning orders!
    private static final Logger logger = LogManager.getLogger();

    private static final int SUBSCRIPTION_DAYS = 30;
    private static final double DAY_OVERDUE_FEE = 10;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(Order order) throws ServiceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void placeOrder(Order order, BookService bookService, TransactionManager transactionManager) throws ServiceException {

        try {
            logger.log(Level.DEBUG, "OrderServiceImpl/placeOrder/Transaction started");
            transactionManager.beginTransaction();

            var nowTime = LocalDateTime.now();
            order.setOrderStartDate(nowTime);


            order.setOrderEndDate(order.isOnSubscription() ? nowTime.plusDays(SUBSCRIPTION_DAYS) : LocalDateTime.of(nowTime.toLocalDate(), LocalTime.MAX));

            orderDAO.save(order);

            bookService.decrementBookQuantity(order.getBookId());

            transactionManager.commit();

            logger.log(Level.DEBUG, "OrderServiceImpl/placeOrder/Transaction committed successfully");
        } catch (DaoException e) {
            try {
                transactionManager.rollback();
            } catch (DaoException ex) {
                throw new ServiceException("OrderServiceImpl/error while executing placeOrder method" + "Rollback method didn't work", ex);
            }
            throw new ServiceException("OrderServiceImpl/error while executing placeOrder method ", e);
        } finally {
            transactionManager.endTransaction();
        }
        logger.log(Level.DEBUG, "OrderServiceImpl/saved order:" + order + " successfully");
    }

    @Override
    public List<Order> findAllUsersOrders(long userId, int start, int offset, OrderStatus... orderStatus) throws ServiceException {
        if (orderStatus.length == 0 || orderStatus.length > 3) {
            throw new ServiceException("Error while executing findAllUsersOrders method: orderStatus array must contain from one to three elements");
        }
        try {
            return orderDAO.findAllUsersOrders(userId, start, offset, orderStatus);
        } catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/error while executing findAllUsersOrders method", e);
        }
    }

    @Override
    public List<Order> findAll(int start, int offset) throws ServiceException {
        try {
            return orderDAO.findAll(start, offset);
        } catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/error while executing findAll method", e);
        }
    }

    @Override
    public int countUsersOrders(long userId, OrderStatus... orderStatuses) throws ServiceException {
        try {
            return orderDAO.countUserOrders(userId, orderStatuses);
        } catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/error while executing countUsersOrders method", e);
        }
    }

    @Override
    public double countFine(Order order) {
        long daysPassed = ChronoUnit.DAYS.between(order.getOrderEndDate(), order.getReturnDate() == null ? LocalDateTime.now() : order.getReturnDate());
        if (daysPassed <= 0) {
//            logger.log(Level.DEBUG, "OrderServiceImpl/countFine/ order_id: fine: " + 0);
            return 0;
        }
        double fine = daysPassed * DAY_OVERDUE_FEE;
//        logger.log(Level.DEBUG, "OrderServiceImpl/countFine/ fine: " + fine);
        return fine;
    }

    @Override
    public int countTotalOrders() throws ServiceException {
        try {
            return orderDAO.countTotalOrders();
        } catch (DaoException e) {
            throw new ServiceException("OrderServiceImpl/error while executing countTotalOrders method", e);
        }
    }

    @Override
    public void returnOrder(long orderId, BookService bookService, UserService userService, TransactionManager transactionManager) throws ServiceException {
        try {
            logger.log(Level.DEBUG, "OrderServiceImpl/returnOrder/Transaction started");
            transactionManager.beginTransaction();

            var nowTime = LocalDateTime.now();

            var orderContainer = orderDAO.find(orderId);
            if (orderContainer.isEmpty()) {
                throw new ServiceException("OrderServiceImpl/ order doesn't exists!");
            }
            var order = orderContainer.get();

            order.setReturnDate(nowTime);
            orderDAO.update(order);
            bookService.incrementBookQuantity(order.getBookId());

            transactionManager.commit();

            logger.log(Level.DEBUG, "OrderServiceImpl/returnOrder/Transaction committed successfully");
        } catch (DaoException e) {
            try {
                transactionManager.rollback();
            } catch (DaoException ex) {
                throw new ServiceException("OrderServiceImpl/error while executing returnOrder method" + "Rollback method didn't work", ex);
            }
            throw new ServiceException("OrderServiceImpl/error while executing returnOrder method ", e);
        } finally {
            transactionManager.endTransaction();
        }
        logger.log(Level.DEBUG, "OrderServiceImpl/returnOrder executed successfully ");
    }


    @Override
    public List<Order> findAllByStatus(int start, int offset, OrderStatus... orderStatus) throws ServiceException {
        if (orderStatus.length == 0 || orderStatus.length > 3) {
            throw new ServiceException("Error while executing findAllByStatus method: orderStatus array must contain from one to three elements");
        }
        try {
            return orderDAO.findAllByStatus(start, offset, orderStatus);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing findAllByStatus method", e);
        }

    }

    @Override
    public int countOrdersByStatus(OrderStatus... orderStatus) throws ServiceException {
        if (orderStatus.length == 0 || orderStatus.length > 3) {
            throw new ServiceException("Error while executing findAllByStatus method: orderStatus array must contain from one to three elements");
        }
        try {
            return orderDAO.countOrdersByStatus(orderStatus);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing countOrdersByStatus method", e);
        }
    }


    @Override
    public boolean acceptOrder(long id) throws ServiceException {
        try {
            logger.log(Level.DEBUG, "OrderServiceImpl/acceptOrder/Transaction started");
            var orderContainer = orderDAO.find(id);
            if (orderContainer.isEmpty()) {
                throw new ServiceException("OrderServiceImpl/ acceptOrder order doesn't exists!");
            }
            var order = orderContainer.get();
            order.setOrderStatus(OrderStatus.ACCEPTED);
            var nowTime = LocalDateTime.now();

            order.setOrderStartDate(nowTime);
            order.setOrderEndDate(order.isOnSubscription() ? nowTime.plusDays(SUBSCRIPTION_DAYS) :
                    LocalDateTime.of(nowTime.toLocalDate(), LocalTime.MAX));

            return orderDAO.update(order);

        } catch (DaoException e) {

            throw new ServiceException("Error while executing acceptOrder method", e);
        }
    }

    @Override
    public void declineOrder(long id, BookService bookService, TransactionManager transactionManager) throws ServiceException {
        try {
            logger.log(Level.DEBUG, "OrderServiceImpl/declineOrder/Transaction started");
            transactionManager.beginTransaction();

            var orderContainer = orderDAO.find(id);
            if (orderContainer.isEmpty()) {
                throw new ServiceException("OrderServiceImpl/ order doesn't exists!");
            }
            var order = orderContainer.get();

            orderDAO.setOrderStatus(order.getOrderId(), OrderStatus.REJECTED);

            bookService.incrementBookQuantity(order.getBookId());

            transactionManager.commit();

            logger.log(Level.DEBUG, "OrderServiceImpl/declineOrder/Transaction committed successfully");
        } catch (DaoException e) {
            try {
                transactionManager.rollback();
            } catch (DaoException ex) {
                throw new ServiceException("OrderServiceImpl/error while executing declineOrder method" + "Rollback method didn't work", ex);
            }
            throw new ServiceException("OrderServiceImpl/error while executing declineOrder method ", e);
        } finally {
            transactionManager.endTransaction();
        }
        logger.log(Level.DEBUG, "OrderServiceImpl/declineOrder executed successfully ");
    }


    @Override
    public boolean update(Order order) throws ServiceException {
        throw new UnsupportedOperationException();
    }
}
