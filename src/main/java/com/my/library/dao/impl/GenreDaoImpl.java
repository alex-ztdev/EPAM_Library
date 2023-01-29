package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.GenreDAO;
import com.my.library.dao.constants.columns.GenreColumns;
import com.my.library.dao.constants.queries.GenreQueries;
import com.my.library.entities.Genre;
import com.my.library.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDaoImpl implements GenreDAO {
    private static final ConnectionPool dbm = ConnectionPool.getInstance();
    private static volatile GenreDaoImpl INSTANCE;

    private GenreDaoImpl() {
    }

    public static GenreDaoImpl getInstance() {
        GenreDaoImpl instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (GenreDaoImpl.class) {
            if (instance == null) {
                instance = new GenreDaoImpl();
            }
            return instance;
        }
    }

    @Override
    public List<Genre> findAll() throws DaoException {

        List<Genre> GenresList = new ArrayList<>();

        try (var connection = dbm.get();
             var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(GenreQueries.FIND_ALL_GENRES) ) {
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
