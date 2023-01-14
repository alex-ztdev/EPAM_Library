package com.library.connection_pool;

import com.library.exceptions.DBManagerException;
import com.library.utils.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DBManager {

    private static volatile DBManager dbm;
    private static final String URL = "db.url";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";
    private static final String POOL_SIZE = "db.pool.size";


    private static final Integer DEFAULT_POOL_SIZE = 10;
    private BlockingQueue<Connection> pool;


    private void initConnectionPool() {
        var poolSizePropertyVal = PropertiesUtil.get(POOL_SIZE);
        var poolSize = poolSizePropertyVal == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSizePropertyVal);
        pool = new ArrayBlockingQueue<>(poolSize);


        try {
            for (int i = 0; i < poolSize; i++) {
                pool.add(new ConnectionProxy(DriverManager.getConnection(PropertiesUtil.get(URL),
                        PropertiesUtil.get(USERNAME),
                        PropertiesUtil.get(PASSWORD)),
                        pool));
            }
        } catch (SQLException e) {
            throw new DBManagerException(e);
        }
    }

    private DBManager() {
        initConnectionPool();
    }

    public static DBManager getInstance() {
        DBManager instance = dbm;
        if (instance != null) {
            return instance;
        }
        synchronized (DBManager.class) {
            if (instance == null) {
                instance = new DBManager();
            }
            return instance;
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
            throw new DBManagerException(e);
        }
    }
}
