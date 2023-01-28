package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.controller.command.constant.BooksOrderDir;
import com.my.library.dao.AuthorDAO;
import com.my.library.dao.BookDAO;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.dao.constants.columns.BooksColumns;
import com.my.library.dao.constants.queries.BookQueries;
import com.my.library.entities.Author;
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

    private final AuthorDAO authorDAO = AuthorDaoImpl.getInstance();

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

    private Book buildBook(ResultSet rs) throws SQLException, DaoException {
        return new Book(rs.getLong(BooksColumns.ID),
                rs.getString(BooksColumns.TITLE),
                rs.getString(BooksColumns.PUBLISHER),
                rs.getString(BooksColumns.GENRE),
                rs.getInt(BooksColumns.PAGE_NUMBER),
                rs.getDate(BooksColumns.PUBLICATION_DATE).toLocalDate(),
                authorDAO.find(rs.getLong(BooksColumns.AUTHOR_ID)).orElse(Author.UNKNOWN_AUTHOR));
    }


    @Override
    public List<Book> findAll(int start, int fetchNext, BooksOrderTypes orderBy, BooksOrderDir dir, boolean includeRemoved) throws DaoException {
        List<Book> bookList = new ArrayList<>();
        var unformattedQ = includeRemoved ? BookQueries.FIND_ALL_BOOKS_PAGINATION : BookQueries.FIND_ALL_NOT_REMOVED_BOOKS_PAGINATION;

        String query = String.format(unformattedQ, orderBy.getOrderBy(), dir);
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(query)) {

            int k = 1;
            statement.setInt(k++, start);
            statement.setInt(k, fetchNext);

            try (var rs = statement.executeQuery()) {
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
    public void save(Book book) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            statement.setString(k++, book.getTitle());
            statement.setString(k++, book.getPublisherTitle());
            statement.setString(k++, book.getGenre());
            statement.setInt(k++, book.getPageNumber());
            statement.setDate(k++, Date.valueOf(book.getPublicationDate()));
            statement.setLong(k, book.getAuthor().getAuthorId());

            statement.executeUpdate();
            try (var keysRS = statement.getGeneratedKeys()) {
                if (keysRS.next()) {
                    book.setBookId(keysRS.getLong(1));
                }
            }

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
            statement.setLong(k, book.getBookId());

            var updatedRows = statement.executeUpdate();

            return updatedRows == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Book book) throws DaoException {
        if (book != null) {
            deleteById(book.getBookId());
        }
    }

    @Override
    public void deleteById(long id) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.SET_BOOK_TO_REMOVED)) {
            int k = 1;
            statement.setBoolean(k++, true);
            statement.setLong(k, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countBooks(boolean includeRemoved) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(includeRemoved ? BookQueries.COUNT_ALL_BOOK_RECORDS : BookQueries.COUNT_NOT_REMOVED_BOOK_RECORDS)) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isRemoved(long id) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.IS_REMOVED_BOOK)) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getBoolean(BooksColumns.IS_REMOVED);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int getQuantity(long id) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(BookQueries.GET_QUANTITY)) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(BooksColumns.QUANTITY);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


}
