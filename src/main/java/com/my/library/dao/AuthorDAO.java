package com.my.library.dao;

import com.my.library.entities.Author;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface AuthorDAO {
    Optional<Author> find(long id) throws DaoException;

    List<Author> findAll() throws DaoException;

    Author save(Author author) throws DaoException;

    boolean update(Author author) throws DaoException;

    Optional<Author> findByNames(String firstName, String secondName) throws DaoException;
}
