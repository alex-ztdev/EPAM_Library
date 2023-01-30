package com.my.library.services;

import com.my.library.dao.DaoFactory;
import com.my.library.dao.impl.BookDaoImpl;
import com.my.library.dao.impl.GenreDaoImpl;
import com.my.library.dao.impl.PublisherDaoImpl;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.services.impl.*;

import java.sql.Connection;

public class ServiceFactory {
    private Connection connection;

    private DaoFactory daoFactory;
    public ServiceFactory(Connection connection) {
        this.connection = connection;
        this.daoFactory = new DaoFactory(connection);
    }

    public UserService getUserService() {
        return new UserServiceImpl(daoFactory.getUserDao());
    }

    public BookService getBookService() {
        return new BookServiceImpl(daoFactory.getBookDao());
    }

    public AuthorService getAuthorService() {
        return new AuthorServiceImpl(daoFactory.getAuthorDao());
    }

    public static PublisherService getPublisherService() {
        return new PublisherServiceImpl(PublisherDaoImpl.getInstance());
    }
    public static GenreService getGenreService() {
        return new GenreServiceImpl(GenreDaoImpl.getInstance());
    }

}
