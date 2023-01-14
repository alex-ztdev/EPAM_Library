package com.library.dao.impl;


import com.library.connection_pool.DBManager;
import com.library.dao.AuthorDAO;
import com.library.dao.constants.columns.AuthorsColumns;
import com.library.dao.constants.queries.AuthorQueries;
import com.library.entities.Author;
import com.library.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl implements AuthorDAO {
    private static volatile AuthorDaoImpl INSTANCE;
    private static final DBManager dbm = DBManager.getInstance();

    private static final String URL = "db.url";

    private AuthorDaoImpl() {
    }

    public static AuthorDaoImpl getInstance() {
        AuthorDaoImpl instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (AuthorDaoImpl.class) {
            if (instance == null) {
                instance = new AuthorDaoImpl();
            }
            return instance;
        }
    }

    @Override
    public Optional<Author> find(long id) {
        Author author = null;
        try (var connection = dbm.get()) {
            try (var statement = connection.prepareStatement(AuthorQueries.GET_AUTHOR_BY_ID)) {
                statement.setLong(1, id);

                var rs = statement.executeQuery();
                if(rs.next()) {
                    author = buildAuthor(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return author == null ? Optional.empty() : Optional.of(author);
    }

    private Author buildAuthor(ResultSet rs) throws SQLException {
        var author = new Author();
        author.setId(rs.getLong(AuthorsColumns.ID));
        author.setFirstName(rs.getString(AuthorsColumns.FIRST_NAME));
        author.setSecondName(rs.getString(AuthorsColumns.SECOND_NAME));
        return author;
    }

    @Override
    public List<Author> findAll() {
        List<Author> authorList = new ArrayList<>();
        try (var connection = dbm.get()) {
            try (var statement = connection.createStatement()) {
                var rs = statement.executeQuery(AuthorQueries.GET_ALL_AUTHORS);
                while(rs.next()) {
                    authorList.add(buildAuthor(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return authorList;
    }

    @Override
    public void save(Author entity) {

    }

    @Override
    public void update(Author entity) {

    }

    @Override
    public void delete(Author entity) {

    }
}
