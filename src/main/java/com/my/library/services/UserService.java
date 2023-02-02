package com.my.library.services;

import com.my.library.dao.constants.UserRole;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service<User> {
    Optional<User> authenticate(String login, String password) throws ServiceException;

    List<String> canBeRegistered(User user) throws ServiceException;

    List<User> findAll(int start, int offset) throws ServiceException;

    int countTotalUsers() throws ServiceException;

    void blockUser(long userId) throws ServiceException;

    void unblockUser(long userId) throws ServiceException;

    void setUserRole(long userId, UserRole newUserRole) throws ServiceException;
}
