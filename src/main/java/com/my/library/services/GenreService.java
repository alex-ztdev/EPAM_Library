package com.my.library.services;

import com.my.library.entities.Genre;
import com.my.library.exceptions.ServiceException;

import java.util.List;

public interface GenreService {
    List<Genre> findAll() throws ServiceException;
}
