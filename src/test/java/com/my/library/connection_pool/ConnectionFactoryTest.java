package com.my.library.connection_pool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.DriverManager;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConnectionFactoryTest {
    private static MockedStatic<DriverManager> mockedDriverManager;
    private static MockedStatic<Class> mockedClass;

    @BeforeEach
    void init() {
        mockedDriverManager = mockStatic(DriverManager.class);
        mockedClass = mockStatic(Class.class);
    }

    @AfterEach
    void close() {
        mockedDriverManager.close();

        mockedClass.close();
    }

    @Test
    void initializationBlock_InvalidDriver_ShouldThrowExceptionInInitializerError() {
        var construction = mockConstruction(Properties.class, (mock, context) -> {
            doReturn("jdbc:sqlserver://localhost:1433;database=Library;trustServerCertificate=true").when(mock).getProperty("db.url");
            doReturn("sa").when(mock).getProperty("db.username");
            doReturn("saaccount").when(mock).getProperty("db.password");
            doReturn("invalidDriver").when(mock).getProperty("db.driver");
        });

        assertThatThrownBy(ConnectionFactory::getConnection)
                .isExactlyInstanceOf(ExceptionInInitializerError.class);

        construction.close();
    }

    @Test
    void initializationBlock_valid_ShouldThrowExceptionInInitializerError() throws Exception {

        when(DriverManager.getConnection("com.microsoft.sqlserver.jdbc.SQLServerDriver", "sa", "saaccount")).thenReturn(null);

        var construction = mockConstruction(Properties.class, (mock, context) -> {
            doReturn("jdbc:sqlserver://localhost:1433;database=Library;trustServerCertificate=true").when(mock).getProperty("db.url");
            doReturn("sa").when(mock).getProperty("db.username");
            doReturn("saaccount").when(mock).getProperty("db.password");
            doReturn("com.microsoft.sqlserver.jdbc.SQLServerDriver").when(mock).getProperty("db.driver");
        });
        construction.close();
    }
}