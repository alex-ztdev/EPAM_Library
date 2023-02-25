package com.my.library.connection_pool;

import com.my.library.exceptions.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final Logger logger = LogManager.getLogger();
    private static final Integer POOL_SIZE = 10;
    private static volatile ConnectionPool instance;

    private BlockingQueue<ConnectionProxy> pool;

    private void initConnectionPool() {
        pool = new ArrayBlockingQueue<>(POOL_SIZE);

        int i;
        for (i = 1; i <= POOL_SIZE; i++) {
            try {
                Connection connection = ConnectionFactory.getConnection();
                var isAdded = pool.add((ConnectionProxy) connection);
                logger.log(Level.INFO, "connection: " + connection + " added to pool: " + isAdded);
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
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = pool.take();
        } catch (InterruptedException e) {
            logger.log(Level.WARN, "Interrupted exception while getting connection!", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    void retrieveConnection(ConnectionProxy connection) {
        var isPut = pool.offer(connection);
        if (!isPut) {
            logger.log(Level.ERROR, "Error while retrieve connection");
        }
    }

    public void destroyPool() {
        int poolSize = pool.size();
        for (int i = 0; i < poolSize; i++) {
            try {
                var connection = pool.take();
                connection.reallyClose();
                logger.log(Level.INFO, "Connection " + connection + "is closed");
            } catch (InterruptedException | SQLException e) {
                logger.log(Level.ERROR, "Error while destroying pool");
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Error while deregistrating drivers" + e.getMessage(), e);
            }
        });
    }
}


