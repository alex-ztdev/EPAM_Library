package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.AbstractDao;
import com.my.library.dao.PublisherDAO;
import com.my.library.dao.constants.columns.PublisherColumns;
import com.my.library.dao.constants.queries.PublisherQueries;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherDaoImpl extends AbstractDao implements PublisherDAO {
    public PublisherDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Publisher> findAll() throws DaoException {

        List<Publisher> publishersList = new ArrayList<>();

        try (var statement = connection.createStatement()) {
            try (var rs = statement.executeQuery(PublisherQueries.FIND_ALL_PUBLISHERS)) {
                while (rs.next()) {
                    publishersList.add(buildPublisher(rs));
                }
            }
            return publishersList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Publisher buildPublisher(ResultSet rs) throws SQLException {
        return new Publisher(rs.getLong(PublisherColumns.PUBLISHER_ID),
                rs.getString(PublisherColumns.PUBLISHER_TITLE));
    }
}
