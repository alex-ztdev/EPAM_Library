package com.library.connection_pool;

import com.library.utils.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static volatile DBManager dbm;
    private static final String URL = "db.url";
    private static final String POOL_SIZE = "db.url";



    private DBManager() {}

    public static DBManager getInstance(){
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

    public Connection get() {
        try{
            return DriverManager.getConnection(PropertiesUtil.get(URL));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
