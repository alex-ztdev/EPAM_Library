package com.my.library.dao.impl;

import com.my.library.dao.OrderDAO;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDAO {

    @Override
    public Optional<Order> find(long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() throws DaoException {
        return null;
    }

    @Override
    public void save(Order order) throws DaoException {

    }

    @Override
    public boolean update(Order order) throws DaoException {
        return false;
    }

    @Override
    public void delete(Order order) throws DaoException {

    }
}
