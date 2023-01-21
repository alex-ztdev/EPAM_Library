package com.my.library.services;

import com.my.library.entities.Book;

import com.my.library.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface Service <T> {
    Optional<T> find(long id) throws ServiceException;

    List<T> findAll() throws ServiceException;

    void save(T entity) throws ServiceException;

    void update(T entity) throws ServiceException;
}
