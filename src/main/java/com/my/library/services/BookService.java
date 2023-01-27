package com.my.library.services;

import com.my.library.entities.Book;
import com.my.library.exceptions.ServiceException;

import java.util.List;

public interface BookService extends Service<Book> {
    void delete(Book book) throws ServiceException;

    void deleteById(long id) throws ServiceException;

    List<Book> findAll(int pageNum) throws ServiceException;

    int countBooks() throws ServiceException;

}

