package com.my.library.services.impl;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.UserDAO;
import com.my.library.dao.constants.UserRole;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Encrypt;
import com.my.library.utils.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{
    private static final Logger logger = LogManager.getLogger();

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> find(long id) throws ServiceException {
        try {
            return userDAO.find(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while searching user by id", e);
        }
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return userDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error while searching all users", e);
        }
    }

    @Override
    public User save(User user) throws ServiceException {
        try {
            if (canBeRegistered(user).isEmpty()) {
                user.setPassword(Encrypt.encryptWithSha512Hex(user.getPassword()));
                return userDAO.save(user);
            } else {
                throw new ServiceException("User already exists!" + canBeRegistered(user));
            }
        } catch (DaoException e) {
            throw new ServiceException("Error in save method", e);
        }
    }

    @Override
    public boolean update(User user) throws ServiceException {
        try {
            return userDAO.update(user);
        } catch (DaoException e) {
            throw new ServiceException("Error in update method ", e);
        }
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        logger.log(Level.INFO, "authenticate method invoked for user with login: " + login);
        var userValidator = new UserValidator();
        if (!userValidator.isValidLogin(login) || !userValidator.isValidPassword(password)) {
            return Optional.empty();
        }
        try {
            return userDAO.authenticate(login, Encrypt.encryptWithSha512Hex(password));
        } catch (DaoException e) {
            throw new ServiceException("Error in authenticate method", e);
        }
    }

    @Override
    public List<String> canBeRegistered(User user) throws ServiceException {
        List<String> exceptionsList = new UserValidator().validateUserParameters(user);
        if (!exceptionsList.isEmpty()) {
            return exceptionsList;
        }
        try {
            if (userDAO.findByEmail(user.getEmail()).isPresent()) {
                exceptionsList.add(UserParameters.USER_EMAIL_ALREADY_EXISTS);
            }
            if (userDAO.findByLogin(user.getLogin()).isPresent()) {
                exceptionsList.add(UserParameters.USER_LOGIN_ALREADY_EXISTS);
            }
            if (user.getPhoneNumber() != null && userDAO.findByPhone(user.getPhoneNumber()).isPresent()) {
                exceptionsList.add(UserParameters.USER_PHONE_ALREADY_EXISTS);
            }
            return exceptionsList;
        } catch (DaoException e) {
            throw new ServiceException("Error while checking if user with such data already exists", e);
        }
    }

    @Override
    public List<User> findAll(int start, int offset) throws ServiceException {
        try {
            return userDAO.findAll(start, offset);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing findAll(int, int) method", e);
        }
    }

    @Override
    public int countTotalUsers() throws ServiceException {
        try {
            return userDAO.countTotalUsers();
        } catch (DaoException e) {
            throw new ServiceException("Error while executing countTotalUsers method", e);
        }

    }

    @Override
    public boolean blockUser(long userId) throws ServiceException {
        try {
            return userDAO.block(userId);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing blockUser method", e);
        }
    }

    @Override
    public boolean unblockUser(long userId) throws ServiceException {
        try {
            return userDAO.unblock(userId);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing unblockUser method", e);
        }
    }

    @Override
    public void setUserRole(long id, UserRole newUserRole) throws ServiceException {
        try {
            userDAO.setUserRole(id, newUserRole);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing setUserRole method", e);
        }
    }

    @Override
    public List<User> findAllReaders(int start, int offset) throws ServiceException {
        try {
            return userDAO.findAllReaders(start, offset);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing findAllReaders method", e);
        }
    }

    @Override
    public int countReaders(boolean includeBlocked) throws ServiceException {
        try {
            return userDAO.countReaders(includeBlocked);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing countReaders method", e);
        }
    }

    @Override
    public boolean isBanned(long userId) throws ServiceException {
        try {
            return userDAO.isBanned(userId);
        } catch (DaoException e) {
            throw new ServiceException("Error while executing isBanned method", e);
        }
    }
}
