package com.my.library.dao.impl;


import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.AuthorDAO;
import com.my.library.dao.constants.columns.AuthorsColumns;
import com.my.library.dao.constants.queries.AuthorQueries;
import com.my.library.entities.Author;
import com.my.library.exceptions.DaoException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl implements AuthorDAO {
    private static final ConnectionPool dbm = ConnectionPool.getInstance();
    private static volatile AuthorDaoImpl INSTANCE;

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
    public Optional<Author> find(long id) throws DaoException {
        Author author = null;
        try (var connection = dbm.get()) {
            try (var statement = connection.prepareStatement(AuthorQueries.GET_AUTHOR_BY_ID)) {
                statement.setLong(1, id);

                try(var rs = statement.executeQuery();){
                    if (rs.next()) {
                        author = buildAuthor(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return author == null ? Optional.empty() : Optional.of(author);
    }

    private Author buildAuthor(ResultSet rs) throws SQLException {
        var author = new Author();
        author.setAuthorId(rs.getLong(AuthorsColumns.ID));
        author.setFirstName(rs.getString(AuthorsColumns.FIRST_NAME));
        author.setSecondName(rs.getString(AuthorsColumns.SECOND_NAME));
        author.setBirthDate(rs.getDate(AuthorsColumns.BIRTH_DATE).toLocalDate());
        return author;
    }

    @Override
    public List<Author> findAll() throws DaoException {
        List<Author> authorList = new ArrayList<>();
        try (var connection = dbm.get();
             var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(AuthorQueries.GET_ALL_AUTHORS);) {
                while (rs.next()) {
                    authorList.add(buildAuthor(rs));
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return authorList;
    }

    @Override
    public Author save(Author author) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(AuthorQueries.INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getSecondName());
            statement.setDate(3, Date.valueOf(author.getBirthDate()));

            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    author.setAuthorId(generatedKeys.getLong(1));
                }
            }
            return author;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Author author) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(AuthorQueries.UPDATE_AUTHOR_BY_ID)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getSecondName());
            statement.setDate(3, Date.valueOf(author.getBirthDate()));

            var updateRes = statement.executeUpdate();
            return updateRes == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}
