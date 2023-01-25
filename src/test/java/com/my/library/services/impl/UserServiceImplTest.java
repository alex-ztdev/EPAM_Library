package com.my.library.services.impl;

import com.my.library.dao.UserDAO;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;


class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;


    @BeforeEach
    void prepare() {
        this.userDAO = Mockito.mock(UserDAO.class);
        this.userService = new UserServiceImpl(userDAO);
    }
    @Test
    void find() {
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void authenticate() {
    }

    @Test
    void canBeRegistered() {
    }
}