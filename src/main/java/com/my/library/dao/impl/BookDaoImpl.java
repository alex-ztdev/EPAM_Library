package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.BookDAO;
import com.my.library.dao.constants.columns.BooksColumns;
import com.my.library.dao.constants.queries.BookQueries;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDAO {
    private static final ConnectionPool dbm = ConnectionPool.getInstance();
    private static volatile BookDaoImpl INSTANCE;

    private BookDaoImpl() {
    }

    public static BookDaoImpl getInstance() {
        BookDaoImpl instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (BookDaoImpl.class) {
            if (instance == null) {
                instance = new BookDaoImpl();
            }
            return instance;
        }
    }

    @Override
    public Optional<Book> find(long id) throws DaoException {
        Book book = null;
        try (var connection = dbm.get()) {
            try (var statement = connection.prepareStatement(BookQueries.GET_BOOK_BY_ID)) {
                statement.setLong(1, id);

                var rs = statement.executeQuery();
                if (rs.next()) {
                    book = buildBook(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return book == null ? Optional.empty() : Optional.of(book);
    }

    private Book buildBook(ResultSet rs) throws SQLException {
//        var book = new Book();
//        book.setBookId(rs.getLong(BooksColumns.ID));
//        book.setFirstName(rs.getString(BooksColumns.FIRST_NAME));
//        book.setSecondName(rs.getString(BooksColumns.SECOND_NAME));
//        book.setBirthDate(rs.getDate(BooksColumns.BIRTH_DATE).toLocalDate());
//        return book;
        return null;
    }

    @Override
    public List<Book> findAll() throws DaoException {
//        List<Book> authorList = new ArrayList<>();
//        try (var connection = dbm.get();
//             var statement = connection.createStatement()) {
//
//            try (var rs = statement.executeQuery(BookQueries.GET_ALL_AUTHORS);) {
//                while (rs.next()) {
//                    authorList.add(buildBook(rs));
//                }
//            }
//
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
//        return authorList;
        return null;
    }

    @Override
    public Book save(Book author) throws DaoException {
//        try (var connection = dbm.get();
//             var statement = connection.prepareStatement(BookQueries.INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {
//
//            statement.setString(1, author.getFirstName());
//            statement.setString(2, author.getSecondName());
//            statement.setDate(3, Date.valueOf(author.getBirthDate()));
//
//            statement.executeUpdate();
//
//            try (var generatedKeys = statement.getGeneratedKeys();) {
//                if (generatedKeys.next()) {
//                    author.setBookId(generatedKeys.getLong(1));
//                }
//            }
//            return author;
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
        return null;
    }

    @Override
    public boolean update(Book author) throws DaoException {
//        try (var connection = dbm.get();
//             var statement = connection.prepareStatement(BookQueries.UPDATE_AUTHOR_BY_ID)) {
//
//            statement.setString(1, author.getFirstName());
//            statement.setString(2, author.getSecondName());
//            statement.setDate(3, Date.valueOf(author.getBirthDate()));
//
//            var updateRes = statement.executeUpdate();
//            return updateRes == 1;
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
        return false;
    }

    @Override
    public void delete(Book book) {

    }

}
