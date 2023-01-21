package com.my.library.services.impl;

import com.my.library.dao.UserDAO;
import com.my.library.dao.impl.AuthorDaoImpl;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserService implements Service<User> {
    private static final Logger logger = LogManager.getLogger();

    private final UserDAO userDAO = UserDaoImpl.getInstance();
    private static volatile UserService INSTANCE;

    private UserService() {
    }

    public static UserService getInstance() {
        UserService instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (UserService.class) {
            if (instance == null) {
                instance = new UserService();
            }
            return instance;
        }
    }

    @Override
    public Optional<User> find(long id) throws ServiceException {
        try {
            return userDAO.find(id);
        } catch (DaoException e) {
            throw new ServiceException("Error while searching user by id:" + e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return userDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Error while searching all users by id",e);
        }
    }

    @Override
    public void save(User entity) throws ServiceException {
//        userDAO.save(entity);

    }

    @Override
    public void update(User entity) throws ServiceException {

    }

//    public static void main(String[] args) {
//        String sha256hex = DigestUtils.sha512Hex("admin");
//        System.out.println(sha256hex);
//    }
}
