package com.my.library.dao;

import com.my.library.entities.Order;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface PublisherDAO {
//    Optional<Publisher> find(long id) throws DaoException;

    List<Publisher> findAll() throws DaoException;

//    void save(Publisher publisher) throws DaoException;
//
//    boolean update(Publisher publisher) throws DaoException;
//
//    List<Publisher> getUsersOrders(long id) throws DaoException;
}
