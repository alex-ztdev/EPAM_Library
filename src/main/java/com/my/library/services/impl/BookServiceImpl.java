package com.my.library.services.impl;

import com.my.library.dao.BookDAO;
import com.my.library.dao.UserDAO;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private static final Logger logger = LogManager.getLogger();

    private final BookDAO bookDAO;

    public BookServiceImpl(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public void delete(Book book) throws ServiceException {
//        try () {
//
//        } catch (DaoException e) {
//
//        }
    }

    @Override
    public Optional<Book> find(long id) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() throws ServiceException {
        return null;
    }

    @Override
    public void save(Book entity) throws ServiceException {

    }

    @Override
    public boolean update(Book entity) throws ServiceException {
        return false;
    }
}
