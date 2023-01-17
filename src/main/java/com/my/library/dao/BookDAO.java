package com.my.library.dao;

import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    Optional<Book> find(long id) throws DaoException;

    List<Book> findAll() throws DaoException;

    Book save(Book book) throws DaoException;

    boolean update(Book book) throws DaoException;

    void delete(long id) throws DaoException;

    List<Author> getBookAuthors(long id) throws DaoException;
}
