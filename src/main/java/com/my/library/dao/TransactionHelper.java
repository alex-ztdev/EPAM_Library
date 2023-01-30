package com.my.library.dao;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionHelper {
    private Connection connection = ConnectionPool.getInstance().getConnection();

    public void beginTransaction(AbstractDao abstractDao, AbstractDao... abstractDaos) throws DaoException {
        try {
            connection.setAutoCommit(false);

            abstractDao.setConnection(connection);
            for (var dao : abstractDaos) {
                dao.setConnection(connection);
            }
        } catch (SQLException e) {
            throw new DaoException("Error while executing beginTransaction method", e);
        }
    }

    public void endTransaction() throws DaoException {
        try {
            connection.setAutoCommit(true);
            connection.close();
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
