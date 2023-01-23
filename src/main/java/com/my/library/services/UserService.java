package com.my.library.services;

import com.my.library.entities.User;
import com.my.library.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service<User> {
    Optional<User> authenticate(String login, String password) throws ServiceException;

    List<String> canBeRegistered(User user) throws ServiceException;
}
