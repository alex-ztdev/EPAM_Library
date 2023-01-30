package com.my.library.dao;

import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface AuthorDAO {
    Optional<Author> find(long id) throws DaoException;

    List<Author> findAll() throws DaoException;

    void save(Author entity) throws DaoException;

    boolean update(Author entity) throws DaoException;

//    List<Book> getAuthorBooks(long id) throws DaoException;

}
