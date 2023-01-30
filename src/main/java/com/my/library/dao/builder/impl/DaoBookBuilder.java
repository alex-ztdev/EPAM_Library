package com.my.library.dao.builder.impl;

import com.my.library.dao.builder.Builder;
import com.my.library.dao.constants.columns.BooksColumns;
import com.my.library.entities.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoBookBuilder implements Builder<Book> {
    @Override
    public Book build(ResultSet resultSet) throws SQLException {
        return new Book(resultSet.getLong(BooksColumns.ID),
                resultSet.getString(BooksColumns.TITLE),
                resultSet.getString(BooksColumns.PUBLISHER),
                resultSet.getString(BooksColumns.GENRE),
                resultSet.getInt(BooksColumns.PAGE_NUMBER),
                resultSet.getDate(BooksColumns.PUBLICATION_DATE).toLocalDate(),
                new DaoAuthorBuilder().build(resultSet));
    }
}
