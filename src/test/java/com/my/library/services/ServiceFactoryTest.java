package com.my.library.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;

import com.my.library.dao.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.my.library.dao.DaoFactory;
import com.my.library.dao.GenreDAO;
import com.my.library.dao.OrderDAO;
import com.my.library.services.BookService;
import com.my.library.services.GenreService;
import com.my.library.services.OrderService;
import com.my.library.services.PublisherService;
import com.my.library.services.UserService;
import com.my.library.services.impl.AuthorServiceImpl;
import com.my.library.services.impl.BookServiceImpl;
import com.my.library.services.impl.GenreServiceImpl;
import com.my.library.services.impl.OrderServiceImpl;
import com.my.library.services.impl.PublisherServiceImpl;
import com.my.library.services.impl.UserServiceImpl;
import org.mockito.Mock;

public class ServiceFactoryTest {

    @Mock
    private Connection connection;
    @Mock
    private DaoFactory daoFactory;
    private ServiceFactory serviceFactory;

    @BeforeEach
    public void setUp() {
        connection = mock(Connection.class);
        daoFactory = mock(DaoFactory.class);
        when(daoFactory.getUserDao()).thenReturn(mock(UserDaoImpl.class));
        when(daoFactory.getBookDao()).thenReturn(mock(BookDaoImpl.class));
        when(daoFactory.getAuthorDao()).thenReturn(mock(AuthorDaoImpl.class));
        when(daoFactory.getPublishersDao()).thenReturn(mock(PublisherDaoImpl.class));
        when(daoFactory.getGenreDao()).thenReturn(mock(GenreDaoImpl.class));
        when(daoFactory.getOrderDao()).thenReturn(mock(OrderDAO.class));

        serviceFactory = new ServiceFactory(connection);
    }

    @Test
    public void testGetUserService() {
        UserService userService = serviceFactory.getUserService();

        assertThat(userService).isInstanceOf(UserServiceImpl.class);
    }

    @Test
    public void testGetBookService() {
        BookService bookService = serviceFactory.getBookService();

        assertThat(bookService).isInstanceOf(BookServiceImpl.class);
    }
    @Test
    public void testGetAuthorService() {
        AuthorService authorService = serviceFactory.getAuthorService();

        assertThat(authorService).isInstanceOf(AuthorServiceImpl.class);
    }

    @Test
    public void testGetPublisherService() {
        PublisherService publisherService = serviceFactory.getPublisherService();

        assertThat(publisherService).isInstanceOf(PublisherServiceImpl.class);
    }

    @Test
    public void testGetGenreService() {
        GenreService genreService = serviceFactory.getGenreService();

        assertThat(genreService).isInstanceOf(GenreServiceImpl.class);
    }

    @Test
    public void testGetOrderService() {
        OrderService orderService = serviceFactory.getOrderService();

        assertThat(orderService).isInstanceOf(OrderServiceImpl.class);
    }
}

