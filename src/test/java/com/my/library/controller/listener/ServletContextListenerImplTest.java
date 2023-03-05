package com.my.library.controller.listener;

import com.my.library.connection_pool.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServletContextListenerImplTest {
    @Mock
    private ServletContextEvent servletContextEvent;
    @InjectMocks
    private ServletContextListenerImpl listener;


    @Test
    void contextInitialized_ShouldGetInstanceOfConnectionPool() {

        try (var connectionPoolStaticMock = mockStatic(ConnectionPool.class)) {
            listener.contextInitialized(servletContextEvent);

            connectionPoolStaticMock.verify(ConnectionPool::getInstance);
        }
    }

    @Test
    public void contextDestroyed_ShouldDestroyConnectionPool() {
        try (var connectionPoolStaticMock = mockStatic(ConnectionPool.class)) {
            ConnectionPool connectionPoolMock = mock(ConnectionPool.class);

            connectionPoolStaticMock.when(ConnectionPool::getInstance).thenReturn(connectionPoolMock);

            listener.contextDestroyed(servletContextEvent);

            connectionPoolStaticMock.verify(ConnectionPool::getInstance);
            verify(connectionPoolMock).destroyPool();
        }
    }
}