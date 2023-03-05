package com.my.library.dao;

import com.my.library.dao.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DaoFactory implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger();
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

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error while closing connection in DaoFactory", e);
        }
    }
}
