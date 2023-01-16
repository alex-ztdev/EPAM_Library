package com.my.library.connection_pool;

import com.my.library.exceptions.ConnectionPoolException;
import com.my.library.exceptions.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final Logger logger = LogManager.getLogger();
    private static final Integer POOL_SIZE = 10;
    private static ConnectionPool instance = new ConnectionPool();

    private BlockingQueue<Connection> pool;


    private void initConnectionPool() {
        pool = new ArrayBlockingQueue<>(POOL_SIZE);

        int i;
        for (i = 1; i <= POOL_SIZE; i++) {
            try {
                Connection connection = ConnectionFactory.getConnection();
                var isAdded = pool.add(connection);
                logger.log(Level.INFO, "connection: " + connection + " is added to pool: " + isAdded);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Failed to create connection!" + e.getLocalizedMessage(), e);
            }
        }
        if (pool.size() == 0) {
            logger.log(Level.FATAL, "Failed to initialize connection pool!");
            throw new RuntimeException("Failed to initialize connection pool!");
        }

    }

    private ConnectionPool() {
        initConnectionPool();
    }

    public static ConnectionPool getInstance() {
        ConnectionPool returnedInstance = instance;
        if (returnedInstance != null) {
            return returnedInstance;
        }
        synchronized (ConnectionPool.class) {
            if (returnedInstance == null) {
                returnedInstance = new ConnectionPool();
            }
            return returnedInstance;
        }
    }

//    private Connection open() {
//        try {
//
//            return;
//        } catch (SQLException e) {
//            throw new DBManagerException(e);
//        }
//    }

    public Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            //todo: handle exception
            Thread.currentThread().interrupt();
            try {
                throw new ConnectionPoolException();
            } catch (ConnectionPoolException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
