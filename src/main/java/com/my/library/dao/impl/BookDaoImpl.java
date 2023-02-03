package com.my.library.dao.impl;

import com.my.library.controller.command.constant.BooksOrderDir;
import com.my.library.dao.AbstractDao;
import com.my.library.dao.BookDAO;
import com.my.library.dao.builder.impl.DaoBookBuilder;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.dao.constants.columns.BooksColumns;
import com.my.library.dao.constants.queries.BookQueries;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl extends AbstractDao implements BookDAO {
    private static final Logger logger = LogManager.getLogger();

    public BookDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Optional<Book> find(long id) throws DaoException {
        Book book = null;
        try (var statement = connection.prepareStatement(BookQueries.FIND_BOOK_BY_ID)) {
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
        return new DaoBookBuilder().build(rs);
    }


    @Override
    public List<Book> findAll(int start, int fetchNext, BooksOrderTypes orderBy, BooksOrderDir dir, boolean includeRemoved) throws DaoException {
        List<Book> bookList = new ArrayList<>();
        var unformattedQ = includeRemoved ? BookQueries.FIND_ALL_BOOKS_PAGINATION : BookQueries.FIND_ALL_NOT_REMOVED_BOOKS_PAGINATION;

        String query = String.format(unformattedQ, orderBy.getOrderBy(), dir);
        try (var statement = connection.prepareStatement(query)) {

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
        logger.log(Level.DEBUG, "BookDaoImpl/save method invoked with book: " + book);

        try (var statement = connection.prepareStatement(BookQueries.INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
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
                    logger.log(Level.DEBUG, "BookDaoImpl/save method returned key: " + keysRS.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Book book) throws DaoException {
        try (var statement = connection.prepareStatement(BookQueries.UPDATE_BOOK)) {
            int k = 1;
            statement.setString(k++, book.getTitle());
            statement.setString(k++, book.getPublisherTitle());
            statement.setString(k++, book.getGenre());
            statement.setString(k++, book.getAuthor().getFirstName());
            statement.setString(k++, book.getAuthor().getSecondName());
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
        try (var statement = connection.prepareStatement(BookQueries.SET_BOOK_TO_REMOVED)) {
            int k = 1;
            statement.setLong(k, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countBooks(boolean includeRemoved) throws DaoException {
        try (var statement = connection.createStatement()) {

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
        try (var statement = connection.prepareStatement(BookQueries.IS_REMOVED_BOOK)) {

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
        try (var statement = connection.prepareStatement(BookQueries.GET_QUANTITY)) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(BooksColumns.QUANTITY);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void restore(long id) throws DaoException {
        try (var statement = connection.prepareStatement(BookQueries.RESTORE_BOOK)) {

            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void setBookCopies(int copies, long id) throws DaoException {
        logger.log(Level.DEBUG, "BookDaoImpl/setBookCopies called with params: copies=" + copies + "\t book_id=" + id);

        try (var statement = connection.prepareStatement(BookQueries.SET_BOOK_COPIES)) {

            statement.setInt(1, copies);
            statement.setLong(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean alreadyExists(Book book) throws DaoException {
        try (var statement = connection.prepareStatement(BookQueries.ALREADY_EXISTS)) {
            int k = 1;
            statement.setString(k++, book.getTitle());
            statement.setString(k++, book.getPublisherTitle());
            statement.setString(k++, book.getGenre());
            statement.setInt(k++, book.getPageNumber());
            statement.setObject(k++, book.getPublicationDate());
            statement.setString(k++, book.getAuthor().getFirstName());
            statement.setString(k, book.getAuthor().getSecondName());

            try (var rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void addToStorage(long id, int copies) throws DaoException {
        logger.log(Level.DEBUG, "BookDaoImpl/addToStorage method invoked with book_id=" + id + " copies=" + copies);

        try (var statement = connection.prepareStatement(BookQueries.ADD_TO_STORAGE)) {
            statement.setLong(1, id);
            statement.setInt(2, copies);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void decrementBookQuantity(long id) throws DaoException {
        logger.log(Level.DEBUG, "BookDaoImpl/decrementBookQuantity method invoked with book_id=" + id);

        try (var statement = connection.prepareStatement(BookQueries.DECREMENT_QUANTITY)) {
            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void incrementBookQuantity(long id) throws DaoException {
        try (var statement = connection.prepareStatement(BookQueries.INCREMENT_QUANTITY)) {
            statement.setLong(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Book> findByTitle(String title, int start, int offset, BooksOrderTypes orderBy, BooksOrderDir dir, boolean includeRemoved) throws DaoException {
        List<Book> bookList = new ArrayList<>();
        var unformattedQ = includeRemoved ? BookQueries.FIND_BY_TITLE_INCLUDE_REMOVED : BookQueries.FIND_BY_TITLE;

        String formattedTitle = '%' + title + '%';

        String query = String.format(unformattedQ, orderBy.getOrderBy(), dir);


        try (var statement = connection.prepareStatement(query)) {
            int k = 1;
            statement.setString(k++, formattedTitle);
            statement.setInt(k++, start);
            statement.setInt(k, offset);

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
    public int countFoundByTitle(String title, boolean includeRemoved) throws DaoException {
        try (var statement = connection.prepareStatement(includeRemoved ? BookQueries.COUNT_FIND_BY_TITLE_INCLUDE_REMOVED : BookQueries.COUNT_FIND_BY_TITLE)) {
            String formattedTitle = '%' + title + '%';
            statement.setString(1, formattedTitle);
            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Book> findByAuthor(String author, int start, int offset, BooksOrderTypes orderBy, BooksOrderDir dir, boolean includeRemoved) throws DaoException {
        List<Book> bookList = new ArrayList<>();
        var unformattedQ = includeRemoved ? BookQueries.FIND_BY_AUTHOR_INCLUDE_REMOVED : BookQueries.FIND_BY_AUTHOR;

        String formattedTitle = '%' + author + '%';

        String query = String.format(unformattedQ, orderBy.getOrderBy(), dir);

        try (var statement = connection.prepareStatement(query)) {
            int k = 1;
            statement.setString(k++, formattedTitle);
            statement.setInt(k++, start);
            statement.setInt(k, offset);

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
    public int countFoundByAuthor(String author, boolean includeRemoved) throws DaoException {
        try (var statement = connection.prepareStatement(BookQueries.COUNT_FIND_BY_TITLE)) {
            statement.setString(1, "%" + author + "%");

            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


//    public void update(Book book, int quantity) {
//        throw new UnsupportedOperationException();
//    }
}
