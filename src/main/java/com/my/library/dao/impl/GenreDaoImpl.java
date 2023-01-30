package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.AbstractDao;
import com.my.library.dao.GenreDAO;
import com.my.library.dao.constants.columns.GenreColumns;
import com.my.library.dao.constants.queries.GenreQueries;
import com.my.library.entities.Genre;
import com.my.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDaoImpl extends AbstractDao implements GenreDAO {

    public GenreDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Genre> findAll() throws DaoException {

        List<Genre> GenresList = new ArrayList<>();

        try (var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(GenreQueries.FIND_ALL_GENRES)) {
                while (rs.next()) {
                    GenresList.add(buildGenre(rs));
                }
            }
            return GenresList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Genre buildGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong(GenreColumns.GENRE_ID),
                rs.getString(GenreColumns.GENRE_TITLE));
    }
}
