package com.my.library.services;

import com.my.library.dao.impl.BookDaoImpl;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.services.impl.BookServiceImpl;
import com.my.library.services.impl.UserServiceImpl;

public class ServiceFactory {
    public static UserService getUserService() {
        return new UserServiceImpl(UserDaoImpl.getInstance());
    }

    public static BookService getBookService() {
        return new BookServiceImpl(BookDaoImpl.getInstance());
    }
}
