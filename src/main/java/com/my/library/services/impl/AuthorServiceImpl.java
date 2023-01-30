package com.my.library.services.impl;

import com.my.library.dao.AuthorDAO;
import com.my.library.dao.BookDAO;
import com.my.library.entities.Author;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = LogManager.getLogger();

    private final AuthorDAO authorDAO;

    public AuthorServiceImpl(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    public Optional<Author> findByNames(String firstName, String secondName) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public Optional<Author> find(long id) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public List<Author> findAll() throws ServiceException {
        return null;
    }

    @Override
    public void save(Author entity) throws ServiceException {

    }

    @Override
    public boolean update(Author entity) throws ServiceException {
        return false;
    }
}
