package com.my.library.dao;

import com.my.library.dao.constants.OrderStatus;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Optional<Order> find(long id) throws DaoException;

    List<Order> findAll(int start, int offset) throws DaoException;

    void save(Order order) throws DaoException;

    boolean update(Order order) throws DaoException;

    void delete(Order order) throws DaoException;

    List<Order> findAllUsersOrders(long userId, int start, int offset, OrderStatus... orderStatus) throws DaoException;


    int countUserOrders(long userId, OrderStatus... orderStatuses) throws DaoException;

    int countTotalOrders() throws DaoException;

    List<Order> findAllByStatus(int start, int offset, OrderStatus... orderStatus) throws DaoException;

    int countOrdersByStatus(OrderStatus... orderStatus) throws DaoException;

    boolean setOrderStatus(long id, OrderStatus orderStatus) throws DaoException;
}
