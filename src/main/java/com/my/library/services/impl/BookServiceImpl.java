package com.my.library.services.impl;

import com.my.library.controller.command.constant.OrderDir;
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
    public boolean deleteById(long id) throws ServiceException {
        try {
            return bookDAO.deleteById(id);
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
    public List<Book> findAll(int from, int to, BooksOrderTypes orderBy, OrderDir dir, boolean includeRemoved) throws ServiceException {
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

        } catch (ServiceException | DaoException e) {
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
    public Book save(Book book, int bookCopies, AuthorService authorService, TransactionManager transactionManager) throws ServiceException {
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
            book = bookDAO.save(book);
            logger.log(Level.DEBUG, "BookServiceImpl/save book_id after save:" + book.getBookId());

            bookDAO.addToStorage(book.getBookId(), bookCopies);

            transactionManager.commit();
            logger.log(Level.DEBUG, "BookServiceImpl/save/Transaction committed successfully");

            return book;
        } catch (ServiceException | DaoException e) {
            try {
                transactionManager.rollback();
                logger.log(Level.DEBUG, "BookServiceImpl/save/Transaction rolledBack successfully");
            } catch (DaoException ex) {
                throw new ServiceException("Error while executing rollback in save method BookServiceImpl", e);
            }
            throw new ServiceException("Error while executing save", e);
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
            throw new ServiceException("BookServiceImpl/ error while executing decreaseBookQuantity", e);
        }
    }

    @Override
    public void incrementBookQuantity(long id) throws ServiceException {
        try {
            bookDAO.incrementBookQuantity(id);
        } catch (DaoException e) {
            throw new ServiceException("BookServiceImpl/ error while executing decreaseBookQuantity", e);
        }
    }

    @Override
    public List<Book> findByTitle(String title, int start, int offset, BooksOrderTypes orderBy, OrderDir dir, boolean includeRemoved) throws ServiceException {
        logger.log(Level.DEBUG, "findByTitle invoked with parameters: title=%s start=%s offset=%s orderBy=%s dir=%s includeRemoved=%s".formatted(title, start, offset, orderBy, dir, includeRemoved));
        if (title == null) {
            throw new ServiceException("error while executing findByTitle method: title may not be null!");
        }
        if (start < 0) {
            throw new ServiceException("error while executing findByTitle method: start may not be negative!");
        }
        if (offset < 0) {
            throw new ServiceException("error while executing findByTitle method: offset  must be greater then zero!");
        }

        try {
            return bookDAO.findByTitle(title, start, offset, orderBy, dir, includeRemoved);
        } catch (DaoException e) {
            throw new ServiceException("BookServiceImpl/ error while executing findByTitle", e);
        }
    }

    @Override
    public int countFoundByTitle(String title, boolean includeRemoved) throws ServiceException {
        if (title == null) {
            throw new ServiceException("error while executing countFoundByTitle method: title may not be null!");
        }
        try {
            return bookDAO.countFoundByTitle(title, includeRemoved);
        } catch (DaoException e) {
            throw new ServiceException("error while executing countFoundByTitle", e);
        }
    }

    @Override
    public List<Book> findByAuthor(String author, int start, int offset, BooksOrderTypes orderBy, OrderDir orderDir, boolean includeRemoved) throws ServiceException {
        if (author == null) {
            throw new ServiceException("error while executing findByAuthor method: author may not be null!");
        }
        if (start < 0) {
            throw new ServiceException("error while executing findByAuthor method: start may not be negative!");
        }
        if (offset < 0) {
            throw new ServiceException("error while executing findByAuthor method: offset  must be greater then zero!");
        }
        try {
            return bookDAO.findByAuthor(author, start, offset, orderBy, orderDir, includeRemoved);
        } catch (DaoException e) {
            throw new ServiceException("error while executing findByAuthor method", e);
        }

    }

    @Override
    public int countFoundByAuthor(String author, boolean includeRemoved) throws ServiceException {
        if (author == null) {
            throw new ServiceException("error while executing countByAuthor method: author may not be null!");
        }
        try {
            return bookDAO.countFoundByAuthor(author, includeRemoved);
        } catch (DaoException e) {
            throw new ServiceException("error while executing countByAuthor method",e);
        }
    }
}
