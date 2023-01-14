package com.library.connection_pool;

import com.library.utils.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DBManager {

    private static volatile DBManager dbm;
    private static final String URL = "db.url";
    private static final String POOL_SIZE = "db.pool.size";

    private static final Integer DEFAULT_POOL_SIZE = 10;
    private BlockingQueue<Connection> pool;


    private void initConnectionPool() {
        var poolSizeProperty = PropertiesUtil.get(POOL_SIZE);
        var poolSize = poolSizeProperty == null ? DEFAULT_POOL_SIZE : Integer.parseInt(POOL_SIZE);
        pool = new ArrayBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            pool.add(open());
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

    private Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URL));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
