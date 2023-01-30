package com.my.library.dao;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private final Connection connection;

    public TransactionManager(Connection connection) {
        this.connection = connection;
    }

    public void beginTransaction() throws DaoException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Error while executing beginTransaction method", e);
        }
    }

    public void endTransaction() throws DaoException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DaoException("Error while executing endTransaction() method", e);
        }
    }

    public void commit() throws DaoException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error while executing commit() method",e);
        }
    }
    public void rollback() throws DaoException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error while executing commit() method",e);
        }
    }
}
