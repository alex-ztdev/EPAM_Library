package com.my.library.connection_pool;

import com.my.library.exceptions.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool instance = new ConnectionPool();

    private static final Integer DEFAULT_POOL_SIZE = 10;
    private BlockingQueue<Connection> pool;


    private void initConnectionPool() {
//        var poolSizePropertyVal = PropertiesUtil.get(POOL_SIZE);
//        var poolSize = poolSizePropertyVal == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSizePropertyVal);
//        pool = new ArrayBlockingQueue<>(poolSize);
//
//
//        try {
//            for (int i = 0; i < poolSize; i++) {
//
//            }
//        } catch (SQLException e) {
//            throw new DBManagerException(e);
//        }
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
            try {
                //todo: handle exception
                throw new ConnectionPoolException(e);
            } catch (ConnectionPoolException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
