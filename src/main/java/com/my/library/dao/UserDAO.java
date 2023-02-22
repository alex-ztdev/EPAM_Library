package com.my.library.dao;

import com.my.library.dao.constants.UserRole;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> find(long id) throws DaoException;

    List<User> findAll() throws DaoException;

    User save(User user) throws DaoException;

    boolean update(User user) throws DaoException;

    boolean block(long id) throws DaoException;

    boolean unblock(long id) throws DaoException;

    Optional<User> authenticate(String login, String password) throws DaoException;

    Optional<User> findByLogin(String login) throws DaoException;

    Optional<User> findByEmail(String email) throws DaoException;

    Optional<User> findByPhone(String phone) throws DaoException;

    List<User> findAll(int start, int offset) throws DaoException;

    int countTotalUsers() throws DaoException;

    void setUserRole(long id, UserRole newUserRole) throws DaoException;

    List<User> findAllReaders(int start, int offset) throws DaoException;

    int countReaders(boolean includeBlocked) throws DaoException;
}
