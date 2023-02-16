package com.my.library.services.impl;

import com.my.library.dao.AuthorDAO;
import com.my.library.dao.BookDAO;
import com.my.library.entities.Author;
import com.my.library.exceptions.DaoException;
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
        try {
            return authorDAO.findByNames(firstName, secondName);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing findByNames method in AuthorServiceImpl",e);
        }
    }

    @Override
    public Optional<Author> find(long id) throws ServiceException {
        try {
            return authorDAO.find(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing find method in AuthorServiceImpl",e);
        }
    }

    @Override
    public List<Author> findAll() throws ServiceException {
        try {
            return authorDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error while executing findAll method in AuthorServiceImpl",e);
        }
    }

    @Override
    public Author save(Author author) throws ServiceException {
        try {
            return authorDAO.save(author);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing save method in AuthorServiceImpl",e);
        }
    }

    @Override
    public boolean update(Author author) throws ServiceException {
        try {
            return authorDAO.update(author);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing update method in AuthorServiceImpl",e);
        }
    }
}
