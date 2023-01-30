package com.my.library.dao;

import com.my.library.dao.impl.AuthorDaoImpl;
import com.my.library.dao.impl.BookDaoImpl;

import java.sql.Connection;

public class DaoFactory {
    private final Connection connection;

    public DaoFactory(Connection connection) {
        this.connection = connection;
    }

    public BookDAO getBookDao() {
        return new BookDaoImpl(connection);
    }
}
