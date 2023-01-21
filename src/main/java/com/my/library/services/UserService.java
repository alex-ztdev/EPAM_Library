package com.my.library.services;

import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service<User> {
    boolean authenticate(String login, String password) throws ServiceException;

}
