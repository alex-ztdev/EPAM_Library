package com.my.library.dao.impl;


import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.AbstractDao;
import com.my.library.dao.AuthorDAO;
import com.my.library.dao.builder.impl.AuthorBuilder;
import com.my.library.dao.constants.columns.AuthorsColumns;
import com.my.library.dao.constants.queries.AuthorQueries;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl extends AbstractDao implements AuthorDAO {
//    private static final ConnectionPool dbm = ConnectionPool.getInstance();
//    private static volatile AuthorDaoImpl INSTANCE;

    public AuthorDaoImpl(Connection connection) {
        this.connection = connection;
    }

//    public static AuthorDaoImpl getInstance() {
//        AuthorDaoImpl instance = INSTANCE;
//        if (instance != null) {
//            return instance;
//        }
//        synchronized (AuthorDaoImpl.class) {
//            if (instance == null) {
//                instance = new AuthorDaoImpl();
//            }
//            return instance;
//        }
//    }

    @Override
    public Optional<Author> find(long id) throws DaoException {
        Author author = null;
        try (var statement = connection.prepareStatement(AuthorQueries.FIND_AUTHOR_BY_ID)) {
            statement.setLong(1, id);

            try (var rs = statement.executeQuery();) {
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
        return new AuthorBuilder().build(rs);
    }

    @Override
    public List<Author> findAll() throws DaoException {
        List<Author> authorList = new ArrayList<>();
        try (var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(AuthorQueries.FIND_ALL_AUTHORS);) {
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
    public void save(Author author) throws DaoException {
        try (var statement = connection.prepareStatement(AuthorQueries.INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getSecondName());

            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    author.setAuthorId(generatedKeys.getLong(1));
                }
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
            statement.setLong(4, author.getAuthorId());

            var updateRes = statement.executeUpdate();
            return updateRes == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

//    @Override
//    public List<Book> getAuthorBooks(long id) throws DaoException {
//        List<Book> bookList = new ArrayList<>();
//
//        try(  var statement = connection.prepareStatement(AuthorQueries.FIND_ALL_AUTHORS_BOOKS)) {
//            BookDaoImpl bookDao = BookDaoImpl.getInstance();
//            statement.setLong(1, id);
//
//            try (var rs = statement.executeQuery()) {
//                while (rs.next()) {
//                    bookDao.find(rs.getLong(AuthorsColumns.AUTHORS_BOOK_ID)).ifPresent(bookList::add);
//                }
//            }
//            return bookList;
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
//    }

}
