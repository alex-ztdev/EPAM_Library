package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.BookDAO;
import com.my.library.dao.constants.columns.AuthorsColumns;
import com.my.library.dao.constants.columns.BooksColumns;
import com.my.library.dao.constants.queries.BookQueries;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;

import java.sql.*;
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
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.FIND_BOOK_BY_ID)) {
            statement.setLong(1, id);

            try (var rs = statement.executeQuery();) {
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
        return new Book(rs.getLong(BooksColumns.ID),
                rs.getString(BooksColumns.TITLE),
                rs.getString(BooksColumns.PUBLISHER),
                rs.getString(BooksColumns.GENRE),
                rs.getInt(BooksColumns.PAGE_NUMBER),
                rs.getDate(BooksColumns.PUBLICATION_DATE).toLocalDate(),
                rs.getBoolean(BooksColumns.IS_AVAILABLE));

    }


    @Override
    public List<Book> findAll() throws DaoException {
        List<Book> bookList = new ArrayList<>();
        try (var connection = dbm.get();
             var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(BookQueries.FIND_ALL_BOOKS)) {
                while (rs.next()) {
                    bookList.add(buildBook(rs));
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return bookList;
    }

    @Override
    public Book save(Book book) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            statement.setString(k++, book.getTitle());
            statement.setString(k++, book.getPublisherTitle());
            statement.setString(k++, book.getGenre());
            statement.setInt(k++, book.getPageNumber());
            statement.setDate(k++, Date.valueOf(book.getPublicationDate()));
            statement.setBoolean(k, book.isAvailable());

            statement.executeUpdate();
            try (var keysRS = statement.getGeneratedKeys()) {
                if (keysRS.next()) {
                    book.setBookId(keysRS.getLong(1));
                }
            }
            return book;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Book book) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.UPDATE_BOOK)) {
            int k = 1;
            statement.setString(k++, book.getTitle());
            statement.setString(k++, book.getPublisherTitle());
            statement.setString(k++, book.getGenre());
            statement.setInt(k++, book.getPageNumber());
            statement.setDate(k++, Date.valueOf(book.getPublicationDate()));
            statement.setBoolean(k++, book.isAvailable());
            statement.setLong(k, book.getBookId());

            var updatedRows = statement.executeUpdate();

            return updatedRows == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.UPDATE_BOOK)) {
            int k = 0;
            statement.setBoolean(k++, false);
            statement.setLong(k, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}
