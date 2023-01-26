package com.my.library.services.impl;

import com.my.library.dao.BookDAO;
import com.my.library.dao.constants.OrderTypes;
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
        try{
            bookDAO.delete(book);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting book in BookServiceImpl", e);
        }
    }
    public void deleteById(long id) throws ServiceException {
        try{
            bookDAO.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting book by id in BookServiceImpl", e);
        }
    }

    @Override
    public Optional<Book> find(long id) throws ServiceException {
        try{
            return bookDAO.find(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting book in BookServiceImpl", e);
        }
    }

    @Override
    public List<Book> findAll() throws ServiceException {
        try{
            return bookDAO.findAll(1, Integer.MAX_VALUE, OrderTypes.BY_TITLE);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting book in BookServiceImpl", e);
        }
    }

    @Override
    public List<Book> findAll(int pageNum) throws ServiceException {
        return null;
    }

    @Override
    public void save(Book book) throws ServiceException {
        try{
            bookDAO.save(book);
        } catch (DaoException e) {
            throw new ServiceException("Error while saving book BookService", e);
        }
    }

    @Override
    public boolean update(Book book) throws ServiceException {
        return false;
    }
}