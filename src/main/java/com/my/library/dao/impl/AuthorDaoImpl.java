package com.my.library.dao.impl;


import com.my.library.dao.AbstractDao;
import com.my.library.dao.AuthorDAO;
import com.my.library.dao.builder.impl.DaoAuthorBuilder;
import com.my.library.dao.constants.queries.AuthorQueries;
import com.my.library.entities.Author;
import com.my.library.exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl extends AbstractDao implements AuthorDAO {
    public AuthorDaoImpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public Optional<Author> find(long id) throws DaoException {
        Author author = null;
        try (var statement = connection.prepareStatement(AuthorQueries.FIND_AUTHOR_BY_ID)) {
            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    author = buildAuthor(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return author == null ? Optional.empty() : Optional.of(author);
    }

    private Author buildAuthor(ResultSet rs) throws SQLException {
        return new DaoAuthorBuilder().build(rs);
    }

    @Override
    public List<Author> findAll() throws DaoException {
        List<Author> authorList = new ArrayList<>();
        try (var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(AuthorQueries.FIND_ALL_AUTHORS)) {
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
        try (var statement = connection.prepareStatement(AuthorQueries.INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getSecondName());

            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                author.setAuthorId(generatedKeys.getLong(1));
                return author;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Author author) throws DaoException {
        try (var statement = connection.prepareStatement(AuthorQueries.UPDATE_AUTHOR_BY_ID)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getSecondName());
            statement.setLong(3, author.getAuthorId());

            var updateRes = statement.executeUpdate();
            return updateRes == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Author> findByNames(String firstName, String secondName) throws DaoException {
        Author author = null;
        try (var statement = connection.prepareStatement(AuthorQueries.FIND_BY_NAMES)) {
            statement.setString(1, firstName);
            statement.setString(2, secondName);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    author = buildAuthor(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return author == null ? Optional.empty() : Optional.of(author);
    }
}
