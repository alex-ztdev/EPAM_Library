package com.my.library.services;

import com.my.library.entities.Author;
import com.my.library.exceptions.ServiceException;

import java.util.Optional;

public interface AuthorService extends Service<Author> {
    Optional<Author> findByNames(String firstName, String secondName) throws ServiceException;
}
