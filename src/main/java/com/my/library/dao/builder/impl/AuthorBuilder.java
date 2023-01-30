package com.my.library.dao.builder.impl;

import com.my.library.dao.builder.Builder;
import com.my.library.dao.constants.columns.AuthorsColumns;
import com.my.library.dao.constants.columns.BooksColumns;
import com.my.library.entities.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorBuilder implements Builder<Author> {

    @Override
    public Author build(ResultSet resultSet) throws SQLException {
        return new Author(resultSet.getLong(BooksColumns.AUTHOR_ID),
                resultSet.getString(AuthorsColumns.FIRST_NAME),
                resultSet.getString(AuthorsColumns.SECOND_NAME)
        );
    }
}
