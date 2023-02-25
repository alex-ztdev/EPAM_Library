package com.my.library.services;

import com.my.library.dao.constants.UserRole;
import com.my.library.entities.User;
import com.my.library.exceptions.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service<User> {
    Optional<User> authenticate(String login, String password) throws ServiceException;

    List<String> canBeRegistered(User user) throws ServiceException;

    List<User> findAll(int start, int offset) throws ServiceException;

    int countTotalUsers() throws ServiceException;

    boolean blockUser(long userId) throws ServiceException;

    boolean unblockUser(long userId) throws ServiceException;

    void setUserRole(long userId, UserRole newUserRole) throws ServiceException;

    List<User> findAllReaders(int start, int offset) throws ServiceException;

    int countReaders(boolean includeBlocked) throws ServiceException;

    boolean isBanned(long userId) throws ServiceException;
}
