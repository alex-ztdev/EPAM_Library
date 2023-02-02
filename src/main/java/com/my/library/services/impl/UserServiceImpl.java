package com.my.library.services.impl;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.UserDAO;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.validator.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
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
            throw new ServiceException("UserServiceImpl/Error while searching user by id", e);
        }
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return userDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("UserServiceImpl/Error while searching all users", e);
        }
    }

    @Override
    public void save(User user) throws ServiceException {
        try {
            if (canBeRegistered(user).isEmpty()) {
                user.setPassword(encryptPassword(user.getPassword()));
                userDAO.save(user);
            } else {
                throw new ServiceException("UserServiceImpl/User already exists!" + canBeRegistered(user));
            }
        } catch (DaoException e) {
            throw new ServiceException("UserServiceImpl/Error in save method", e);
        }
    }

    @Override
    public boolean update(User user) throws ServiceException {
        try {
            return userDAO.update(user);
        } catch (DaoException e) {
            throw new ServiceException("UserServiceImpl/Error in update method ", e);
        }
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        logger.log(Level.INFO, "UserServiceImpl/authenticate method invoked for user with login: " + login);
        var userValidator = new UserValidator();
        if (!userValidator.isValidLogin(login) || !userValidator.isValidPassword(password)) {
            return Optional.empty();
        }
        try {
            return userDAO.authenticate(login, encryptPassword(password));
        } catch (DaoException e) {
            throw new ServiceException("UserServiceImpl/Error in authenticate method", e);
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
        return null;
    }

    private String encryptPassword(String password) {
        return DigestUtils.sha512Hex(password);
    }
}
