package com.my.library.dao;

import com.my.library.controller.command.constant.BooksOrderDir;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    Optional<Book> find(long id) throws DaoException;

    List<Book> findAll(int start, int fetchNext, BooksOrderTypes orderBy, BooksOrderDir dir) throws DaoException;

    void save(Book book) throws DaoException;

    boolean update(Book book) throws DaoException;

    void delete(Book book) throws DaoException;

    void deleteById(long id) throws DaoException;

    int countBooks() throws DaoException;
}
