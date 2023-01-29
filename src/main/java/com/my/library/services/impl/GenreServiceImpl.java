package com.my.library.services.impl;

import com.my.library.dao.GenreDAO;
import com.my.library.entities.Genre;
import com.my.library.entities.Genre;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.GenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GenreServiceImpl implements GenreService {
    private static final Logger logger = LogManager.getLogger();

    private final GenreDAO GenreDAO;

    public GenreServiceImpl(GenreDAO GenreDAO) {
        this.GenreDAO = GenreDAO;
    }

    @Override
    public List<Genre> findAll() throws ServiceException {
        try {
            return GenreDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error while executing findAll method GenreServiceImpl",e);
        }
    }
}
