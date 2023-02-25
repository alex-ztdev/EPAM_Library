package com.my.library.services;

import com.my.library.dao.DaoFactory;
import com.my.library.services.impl.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ServiceFactoryTest {

    @Mock
    private Connection connection;
    @Mock
    private DaoFactory daoFactory;

    @InjectMocks
    private ServiceFactory serviceFactory;


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
    @Test
    public void testClose() {
        serviceFactory.close();

        verify(daoFactory).close();
    }
}

