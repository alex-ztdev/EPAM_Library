package com.my.library.services;

import com.my.library.entities.Book;
import com.my.library.exceptions.ServiceException;

public interface BookService extends Service<Book> {
    void delete(Book book) throws ServiceException;
}

