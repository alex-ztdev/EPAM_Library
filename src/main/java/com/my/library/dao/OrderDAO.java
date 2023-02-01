package com.my.library.dao;

import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Optional<Order> find(long id) throws DaoException;

    List<Order> findAll() throws DaoException;

    void save(Order order) throws DaoException;

    boolean update(Order order) throws DaoException;

    void delete(Order order) throws DaoException;

    List<Order> getUsersOrders(long user_id) throws DaoException;

    List<Order> findAllUsersOrders(long userId, int start, int offset) throws DaoException;
}
