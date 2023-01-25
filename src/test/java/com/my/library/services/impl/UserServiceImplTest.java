package com.my.library.services.impl;

import com.my.library.dao.UserDAO;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


class UserServiceImplTest {

    private UserDAO userDAO;
    private UserService userService;

    private final List<User> validUsersList = Arrays.asList(
            new User(1L, "alexLogin",
                    "alexpass123", UserRole.USER,
                    UserStatus.NORMAL, "alex@gmail.com",
                    "380664444444",
                    "Alex", "Bons"),
            new User(2L, "johnLogin",
                    "johnpass123", UserRole.LIBRARIAN,
                    UserStatus.NORMAL, "john@gmail.com",
                    "380661111111",
                    "John", "Smith"),
            new User(3L, "jimLogin",
                    "jimnpass123", UserRole.ADMINISTRATOR,
                    UserStatus.NORMAL, "jim@gmail.com",
                    "380662222222",
                    "Jim", "Johns")
    );

    private final List<User> invalidUsers = Arrays.asList(
            new User("mikeLogin",
                    "mikenpass", UserRole.ADMINISTRATOR,
                    UserStatus.NORMAL, "mike@gmail.com",
                    "380662222222",
                    "Mike", "Tyson")
    );


    @BeforeEach
    void prepare() throws DaoException {
        this.userDAO = Mockito.mock(UserDAO.class);
        this.userService = new UserServiceImpl(userDAO);

        Mockito.doReturn(Optional.of(validUsersList.get(0))).when(userDAO).find(validUsersList.get(0).getUserId());
        Mockito.doReturn(Optional.of(validUsersList.get(1))).when(userDAO).find(validUsersList.get(1).getUserId());
        Mockito.doReturn(Optional.of(validUsersList.get(2))).when(userDAO).find(validUsersList.get(2).getUserId());
        Mockito.doReturn(Optional.empty()).when(userDAO).find(Mockito.anyInt());

        Mockito.doReturn(validUsersList).when(userDAO).findAll();

        Mockito.doThrow(DaoException.class).when(userDAO).save(validUsersList.get(0));
        Mockito.doThrow(DaoException.class).when(userDAO).save(validUsersList.get(1));
        Mockito.doThrow(DaoException.class).when(userDAO).save(validUsersList.get(2));

        Mockito.doNothing().when(userDAO).save(invalidUsers.get(0));
    }

    @Test
    void findExistingUser() throws ServiceException {
        var userOptional = userService.find(1);
        assertThat(userOptional).isPresent().get().isEqualTo(validUsersList.get(0));

        userOptional = userService.find(2);
        assertThat(userOptional).isPresent().get().isEqualTo(validUsersList.get(2));

        userOptional = userService.find(3);
        assertThat(userOptional).isPresent().get().isEqualTo(validUsersList.get(3));
    }

    @Test
    void findUserThatDoesntExist() throws ServiceException {
        var userOptional = userService.find(Mockito.anyInt());
        assertThat(userOptional).isEmpty();
    }

    @Test
    void findAllUsers() throws ServiceException {
        assertThat(userService.findAll()).isEqualTo(validUsersList);
    }

    @Test
    void saveExistingUser() {
        assertAll(
                () -> assertThrows(ServiceException.class, ()->userService.save(validUsersList.get(0))),
                () -> assertThrows(ServiceException.class, ()->userService.save(validUsersList.get(1))),
                () -> assertThrows(ServiceException.class, ()->userService.save(validUsersList.get(2)))
        );
        //TODO: MOCK CHECK IF REGISTRED METHOD!!!
    }

    @Test
    void saveValidNewUser() {
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