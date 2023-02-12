package com.my.library.services;

import com.my.library.controller.command.constant.OrderDir;
import com.my.library.dao.TransactionManager;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.entities.Book;
import com.my.library.exceptions.ServiceException;

import java.util.List;

public interface BookService extends Service<Book> {
    void deleteById(long id) throws ServiceException;

    List<Book> findAll(int from, int to, BooksOrderTypes orderBy, OrderDir dir, boolean includeRemoved) throws ServiceException;

    int countBooks(boolean includeRemoved) throws ServiceException;

    boolean isRemoved(long id) throws ServiceException;

    int getQuantity(long id) throws ServiceException;

    void restore(long id) throws ServiceException;

    boolean alreadyExists(Book book) throws ServiceException;

    boolean update(Book book,int bookCopies, AuthorService authorService, TransactionManager transactionManager) throws ServiceException;

    void save(Book book, int bookCopies, AuthorService authorService, TransactionManager transactionManager) throws ServiceException;

    void decrementBookQuantity(long id) throws ServiceException;
    void incrementBookQuantity(long id) throws ServiceException;

    List<Book> findByTitle(String title, int start, int offset, BooksOrderTypes orderBy, OrderDir dir, boolean includeRemoved) throws ServiceException;

    int countFoundByTitle(String title, boolean includeRemoved) throws ServiceException;

    List<Book> findByAuthor(String author, int start, int offset, BooksOrderTypes orderBy, OrderDir orderDir, boolean includeRemoved) throws ServiceException;

    int countFoundByAuthor(String author, boolean includeRemoved) throws ServiceException;
}

