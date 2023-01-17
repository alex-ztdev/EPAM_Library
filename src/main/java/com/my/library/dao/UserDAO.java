package com.my.library.dao;

import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> find(long id) throws DaoException;

    List<User> findAll() throws DaoException;

    void save(User user) throws DaoException;

    boolean update(User user) throws DaoException;

    void block(User user) throws DaoException;
    void unblock(User user) throws DaoException;

}
