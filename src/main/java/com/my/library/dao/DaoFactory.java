package com.my.library.dao;

import com.my.library.dao.impl.*;
import com.my.library.entities.Author;

import java.sql.Connection;

public class DaoFactory {
    private final Connection connection;

    public DaoFactory(Connection connection) {
        this.connection = connection;
    }

    public BookDAO getBookDao() {
        return new BookDaoImpl(connection);
    }
    public UserDAO getUserDao() {
        return new UserDaoImpl(connection);
    }
    public GenreDAO getGenreDao() {
        return new GenreDaoImpl(connection);
    }

    public AuthorDAO getAuthorDao() {
        return new AuthorDaoImpl(connection);
    }

    public PublisherDAO getPublishersDao() {
        return new PublisherDaoImpl(connection);
    }
    public OrderDAO getOrderDao() {
        return new OrderDaoImpl(connection);
    }

}
