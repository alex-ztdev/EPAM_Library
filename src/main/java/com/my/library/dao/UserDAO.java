package com.my.library.dao;

import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> find(long id) throws DaoException;

    List<User> findAll() throws DaoException;

    void save(User entity) throws DaoException;

    boolean update(User entity) throws DaoException;

    void block(User entity) throws DaoException;
}
