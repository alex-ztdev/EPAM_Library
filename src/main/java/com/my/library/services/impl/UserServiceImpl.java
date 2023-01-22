package com.my.library.services.impl;

import com.my.library.dao.UserDAO;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.validator.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();

    private final UserDAO userDAO = UserDaoImpl.getInstance();
    private static volatile UserServiceImpl INSTANCE;

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        UserServiceImpl instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (UserServiceImpl.class) {
            if (instance == null) {
                instance = new UserServiceImpl();
            }
            return instance;
        }
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
            throw new ServiceException("Error while searching all users in UserService", e);
        }
    }

    @Override
    public void save(User user) throws ServiceException {
        try {
            userDAO.save(user);
        } catch (DaoException e) {
            throw new ServiceException("Error in save method in UserService", e);
        }
    }

    @Override
    public boolean update(User user) throws ServiceException {
        try {
            return userDAO.update(user);
        } catch (DaoException e) {
            throw new ServiceException("Error in update method in UserService", e);
        }
    }

    @Override
    public boolean authenticate(String login, String password) throws ServiceException {
        if (!UserValidator.isValidLogin(login) || !UserValidator.isValidPassword(password)) {
            return false;
        }
        try {
           return userDAO.authenticate(login, encryptPassword(password));
        } catch (DaoException e) {
            throw new ServiceException("Error in authenticate method in UserService", e);
        }
    }

    private String encryptPassword(String password) {
        return DigestUtils.sha512Hex(password);
    }
}
