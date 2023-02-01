package com.my.library.services.impl;

import com.my.library.controller.command.constant.BooksOrderDir;
import com.my.library.dao.BookDAO;
import com.my.library.dao.TransactionManager;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import com.my.library.services.BookService;
import org.apache.logging.log4j.Level;
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
        try {
            bookDAO.delete(book);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting book in BookServiceImpl", e);
        }
    }

    public void deleteById(long id) throws ServiceException {
        try {
            bookDAO.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting book by id in BookServiceImpl", e);
        }
    }

    @Override
    public Optional<Book> find(long id) throws ServiceException {
        try {
            return bookDAO.find(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while deleting book in BookServiceImpl", e);
        }
    }

    @Override
    public List<Book> findAll() throws ServiceException {
        try {
            return bookDAO.findAll(1, Integer.MAX_VALUE, BooksOrderTypes.BY_TITLE, BooksOrderDir.ASC, false);
        } catch (DaoException e) {
            throw new ServiceException("Error while default findAll in BookServiceImpl", e);
        }
    }

    @Override
    public List<Book> findAll(int from, int to, BooksOrderTypes orderBy, BooksOrderDir dir, boolean includeRemoved) throws ServiceException {
        try {
            return bookDAO.findAll(from, to, orderBy, dir, includeRemoved);
        } catch (DaoException e) {
            throw new ServiceException("Error while findAll method with parameters in BookServiceImpl", e);
        }
    }

    @Override
    public int countBooks(boolean includeRemoved) throws ServiceException {
        try {
            return bookDAO.countBooks(includeRemoved);
        } catch (DaoException e) {
            throw new ServiceException("Error while counting books in BookServiceImpl", e);
        }
    }

    @Override
    public boolean isRemoved(long id) throws ServiceException {
        try {
            return bookDAO.isRemoved(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while exec isRemoved method in BookServiceImpl", e);
        }
    }

    @Override
    public int getQuantity(long id) throws ServiceException {
        try {
            return bookDAO.getQuantity(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while exec getQuantity method in BookServiceImpl", e);
        }
    }

    @Override
    public void restore(long id) throws ServiceException {
        try {
            bookDAO.restore(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while exec restore method in BookServiceImpl", e);
        }
    }

    @Override
    public boolean alreadyExists(Book book) throws ServiceException {
        try {
            return bookDAO.alreadyExists(book);
        } catch (DaoException e) {
            throw new ServiceException("Error while exec alreadyExists method in BookServiceImpl", e);
        }

    }

    @Override
    public boolean update(Book book, int bookCopies, AuthorService authorService, TransactionManager transactionManager) throws ServiceException {
        var operationRes = false;
        logger.log(Level.DEBUG, "Executing update for book: " + book);
        try {
            transactionManager.beginTransaction();

            if (authorService.findByNames(book.getAuthor().getFirstName(), book.getAuthor().getSecondName()).isEmpty()) {
                logger.log(Level.INFO, "BookService update was called for book_id: " + book.getBookId() + " with new Author data:" + book.getAuthor());
                authorService.save(book.getAuthor());
            }
            operationRes = bookDAO.update(book);

            bookDAO.setBookCopies(bookCopies, book.getBookId());

            transactionManager.commit();
            logger.log(Level.DEBUG, "BookServiceImpl/update/Transaction committed: operation_result=" + operationRes);
            return operationRes;

        } catch (DaoException e) {
            try {
                transactionManager.rollback();
                logger.log(Level.DEBUG, "BookServiceImpl/update/Transaction rolledBack successfully");
            } catch (DaoException ex) {
                throw new ServiceException("Error while executing rollback in update method BookServiceImpl", e);
            }
            throw new ServiceException("Error while executing update", e);
        } finally {
            transactionManager.endTransaction();
            logger.log(Level.DEBUG, "BookServiceImpl/update/Transaction ended successfully");
        }
    }

    @Override
    public void save(Book book, int bookCopies, AuthorService authorService, TransactionManager transactionManager) throws ServiceException {
        logger.log(Level.DEBUG, "Executing save for book: " + book);
        try {
            transactionManager.beginTransaction();
            var authorContainer = authorService.findByNames(book.getAuthor().getFirstName(), book.getAuthor().getSecondName());
            if (authorContainer.isEmpty()) {
                logger.log(Level.INFO, "BookServiceImpl/save was called for book_id: " + book.getBookId() + " with new Author data:" + book.getAuthor());
                authorService.save(book.getAuthor());
            } else {
                book.setAuthor(authorContainer.get());
            }
            bookDAO.save(book);
            logger.log(Level.DEBUG, "BookServiceImpl/save book_id after save:" + book.getBookId());

            bookDAO.addToStorage(book.getBookId(), bookCopies);

            transactionManager.commit();
            logger.log(Level.DEBUG, "BookServiceImpl/save/Transaction committed successfully");
        } catch (DaoException e) {
            try {
                transactionManager.rollback();
                logger.log(Level.DEBUG, "BookServiceImpl/save/Transaction rolledBack successfully");
            } catch (DaoException ex) {
                throw new ServiceException("Error while executing rollback in save method BookServiceImpl", e);
            }
            throw new ServiceException("Error while executing update", e);
        } finally {
            transactionManager.endTransaction();
            logger.log(Level.DEBUG, "BookServiceImpl/save/Transaction ended successfully");
        }
    }

    @Override
    public void decrementBookQuantity(long id) throws ServiceException {
        try {
            bookDAO.decrementBookQuantity(id);
        } catch (DaoException e) {
            throw new ServiceException( "BookServiceImpl/ error while executing decreaseBookQuantity", e);
        }
    }

    @Override
    public void incrementBookQuantity(long id) throws ServiceException {
        try {
            bookDAO.decrementBookQuantity(id);
        } catch (DaoException e) {
            throw new ServiceException( "BookServiceImpl/ error while executing decreaseBookQuantity", e);
        }
    }

    @Override
    public void save(Book book) throws ServiceException {
        try {
            bookDAO.save(book);
        } catch (DaoException e) {
            throw new ServiceException("Error while saving book BookService", e);
        }
    }

    @Override
    public boolean update(Book book) throws ServiceException {
        throw new UnsupportedOperationException();
    }

}
