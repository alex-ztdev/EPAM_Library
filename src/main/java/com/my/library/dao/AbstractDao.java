package com.my.library.dao;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.exceptions.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDao{
    protected Connection connection;


    void setConnection(Connection connection) {
        this.connection = connection;
    }

}
