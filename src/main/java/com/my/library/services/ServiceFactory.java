package com.my.library.services;

import com.my.library.dao.DaoFactory;
import com.my.library.services.impl.*;

public class ServiceFactory implements AutoCloseable {
    private final DaoFactory daoFactory;

    public ServiceFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
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

    public PublisherService getPublisherService() {
        return new PublisherServiceImpl(daoFactory.getPublishersDao());
    }

    public GenreService getGenreService() {
        return new GenreServiceImpl(daoFactory.getGenreDao());
    }

    public OrderService getOrderService() {
        return new OrderServiceImpl(daoFactory.getOrderDao());
    }

    @Override
    public void close() {
        daoFactory.close();
    }
}
