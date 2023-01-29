package com.my.library.services;

import com.my.library.entities.Publisher;
import com.my.library.exceptions.ServiceException;

import java.util.List;

public interface PublisherService{
    List<Publisher> findAll() throws ServiceException;
}
