package com.my.library.services;

import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.services.impl.UserServiceImpl;

public class ServiceFactory {
    public UserService getUserService() {
        return new UserServiceImpl(UserDaoImpl.getInstance());
    }

}
