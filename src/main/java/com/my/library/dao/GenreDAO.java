package com.my.library.dao;

import com.my.library.entities.Genre;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.DaoException;

import java.util.List;

public interface GenreDAO {
    List<Genre> findAll() throws DaoException;
}
