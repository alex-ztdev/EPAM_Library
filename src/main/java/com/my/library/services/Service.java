package com.my.library.services;

import com.my.library.entities.User;
import com.my.library.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
    Optional<T> find(long id) throws ServiceException;

    List<T> findAll() throws ServiceException;

    T save(T entity) throws ServiceException;

    boolean update(T entity) throws ServiceException;
}
