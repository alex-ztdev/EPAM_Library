package com.my.library.services;

import com.my.library.entities.User;
import com.my.library.exceptions.ServiceException;

public interface UserService extends Service<User> {
    boolean authenticate(String login, String password) throws ServiceException;

}
