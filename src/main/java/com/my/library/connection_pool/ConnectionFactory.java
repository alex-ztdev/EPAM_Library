package com.my.library.connection_pool;

import com.my.library.exceptions.DaoException;
import com.my.library.utils.PropertiesUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class ConnectionFactory {
    private static final Logger logger = LogManager.getRootLogger();
    private static final String DATABASE_PROPERTIES = "database.properties";
    private static final String PROPERTY_DRIVER = "db.driver";
    private static final String PROPERTY_URL = "db.url";
    private static final String PROPERTY_USERNAME = "db.username";
    private static final String PROPERTY_PASSWORD = "db.password";


    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static final String DATABASE_DRIVER;

    static {

        Properties properties = new Properties();
        try (var inputStream = ConnectionFactory.class.getClassLoader().getResourceAsStream(DATABASE_PROPERTIES)) {
            if (inputStream == null)
                throw new FileNotFoundException("File '" + DATABASE_PROPERTIES + "'" + "not found! ");
            properties.load(inputStream);
            URL = properties.getProperty(PROPERTY_URL);
            USERNAME = properties.getProperty(PROPERTY_USERNAME);
            PASSWORD = properties.getProperty(PROPERTY_PASSWORD);
            DATABASE_DRIVER = properties.getProperty(PROPERTY_DRIVER);
            Class.forName(DATABASE_DRIVER);

        } catch (FileNotFoundException e) {
            logger.log(Level.FATAL, e.getLocalizedMessage(), e);
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.log(Level.FATAL, "IOException " + e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.FATAL, "Driver not found " + e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private ConnectionFactory() {
    }

    static Connection getConnection() throws DaoException {
        try {
            return new ConnectionProxy(DriverManager.getConnection(URL, USERNAME, PASSWORD));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }




}
