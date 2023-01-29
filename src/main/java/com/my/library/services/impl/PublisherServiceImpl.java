package com.my.library.services.impl;

import com.my.library.dao.BookDAO;
import com.my.library.dao.PublisherDAO;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.PublisherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PublisherServiceImpl implements PublisherService {


    private static final Logger logger = LogManager.getLogger();

    private final PublisherDAO publisherDAO;

    public PublisherServiceImpl(PublisherDAO publisherDAO) {
        this.publisherDAO = publisherDAO;
    }


    @Override
    public List<Publisher> findAll() throws ServiceException {
        try {
            return publisherDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error while executing findAll method PublisherServiceImpl",e);
        }
    }
}
