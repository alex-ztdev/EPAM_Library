package com.my.library.services.impl;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.UserDAO;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Encrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;
    private UserService userService;

    private final List<User> validUsersList = List.of(
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
                    "jimnpass123", UserRole.ADMIN,
                    UserStatus.NORMAL, "jim@gmail.com",
                    "380662222222",
                    "Jim", "Johns"),
            new User("existingLogin",
                    "validPassword123", UserRole.ADMIN,
                    UserStatus.NORMAL, "valid_mail2@gmail.com",
                    "380111111112",
                    "Mike", "Tyson")
    );

    private final List<User> usersToSave = List.of(
            new User("validLogin",
                    "validPassword123", UserRole.ADMIN,
                    UserStatus.NORMAL, "valid_mail@gmail.com",
                    "380111111111",
                    "Mike", "Tyson")
    );

    private final List<User> invalidUsers = List.of(
            new User("mikeLogin",
                    "mikepass", UserRole.ADMIN,
                    UserStatus.NORMAL, "mike@gmail.com",
                    "380662222222",
                    "Mike", "Tyson")
    );


    @BeforeEach
    void prepare() throws DaoException {
        this.userDAO = mock(UserDAO.class);
        this.userService = new UserServiceImpl(userDAO);
//
//
//        doReturn(Optional.of(validUsersList.get(0))).when(userDAO).findByPhone(validUsersList.get(0).getPhoneNumber());
//        doReturn(Optional.of(validUsersList.get(1))).when(userDAO).findByPhone(validUsersList.get(1).getPhoneNumber());
//        doReturn(Optional.of(validUsersList.get(2))).when(userDAO).findByPhone(validUsersList.get(2).getPhoneNumber());
//
//
//        doReturn(Optional.of(validUsersList.get(0))).when(userDAO).findByLogin(validUsersList.get(0).getLogin());
//        doReturn(Optional.of(validUsersList.get(1))).when(userDAO).findByLogin(validUsersList.get(1).getLogin());
//        doReturn(Optional.of(validUsersList.get(2))).when(userDAO).findByLogin(validUsersList.get(2).getLogin());
//
//
//        doReturn(Optional.of(validUsersList.get(0))).when(userDAO).findByEmail(validUsersList.get(0).getEmail());
//        doReturn(Optional.of(validUsersList.get(1))).when(userDAO).findByEmail(validUsersList.get(1).getEmail());
//        doReturn(Optional.of(validUsersList.get(2))).when(userDAO).findByEmail(validUsersList.get(2).getEmail());

    }

    @Test
    void find_ExistingUser_ShouldReturnOptionalOfUser() throws ServiceException, DaoException {
        doReturn(Optional.of(validUsersList.get(0))).when(userDAO).find(validUsersList.get(0).getUserId());
        doReturn(Optional.of(validUsersList.get(1))).when(userDAO).find(validUsersList.get(1).getUserId());
        doReturn(Optional.of(validUsersList.get(2))).when(userDAO).find(validUsersList.get(2).getUserId());

        Optional<User> userOptional = userService.find(1);
        assertThat(userOptional).isPresent();

        User user = userOptional.get();

        assertThat(user).isEqualTo(validUsersList.get(0));

        userOptional = userService.find(2);
        assertThat(userOptional).isPresent().get().isEqualTo(validUsersList.get(1));

        userOptional = userService.find(3);
        assertThat(userOptional).isPresent().get().isEqualTo(validUsersList.get(2));
    }

    @Test
    void find_UserThatDoesntExist_ShouldReturnEmptyOptional() throws ServiceException, DaoException {
        when(userDAO.find(anyLong())).thenReturn(Optional.empty());
        var userOptional = userService.find(100);
        assertThat(userOptional).isEmpty();
    }

    @Test
    void find_DaoThrowsException_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(userDAO).find(anyLong());

        assertThatThrownBy(() -> userService.find(anyLong())).isExactlyInstanceOf(ServiceException.class);
    }


    @Test
    void findAllUsers() throws ServiceException, DaoException {
        doReturn(validUsersList).when(userDAO).findAll();

        assertThat(userService.findAll()).isEqualTo(validUsersList);
    }

    @Test
    void findAllUsers_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(userDAO).findAll();

        assertThatThrownBy(() -> userService.findAll()).isExactlyInstanceOf(ServiceException.class);
    }


    @Test
    void saveExistingUser() throws DaoException {
        doThrow(DaoException.class).when(userDAO).save(validUsersList.get(0));
        doThrow(DaoException.class).when(userDAO).save(validUsersList.get(1));
        doThrow(DaoException.class).when(userDAO).save(validUsersList.get(2));


        assertAll(
                () -> assertThrows(ServiceException.class, () -> userService.save(validUsersList.get(0))),
                () -> assertThrows(ServiceException.class, () -> userService.save(validUsersList.get(1))),
                () -> assertThrows(ServiceException.class, () -> userService.save(validUsersList.get(2)))
        );

        verify(userDAO, times(1)).save(validUsersList.get(0));
        verify(userDAO, times(1)).save(validUsersList.get(1));
        verify(userDAO, times(1)).save(validUsersList.get(2));
    }

    @Test
    void saveValidNewUser() throws DaoException {
        User userToSave = usersToSave.get(0);
        assertAll(
                () -> assertDoesNotThrow(() -> userService.save(userToSave))
        );
        verify(userDAO, times(1)).save(userToSave);
    }

    @Test
    void save_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(userDAO).save(usersToSave.get(0));

        assertThatThrownBy(() -> userService.save(usersToSave.get(0))).isExactlyInstanceOf(ServiceException.class);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void authenticate_SuccessfulAuthentication_ShouldReturnOptionalOfUser(int userIndex) throws DaoException, ServiceException {
        doReturn(Optional.of(validUsersList.get(userIndex)))
                .when(userDAO).authenticate(validUsersList.get(userIndex).getLogin(), Encrypt.encryptWithSha512Hex(validUsersList.get(userIndex).getPassword()));

        Optional<User> userOptional = userService.authenticate(validUsersList.get(userIndex).getLogin(), validUsersList.get(userIndex).getPassword());

        assertThat(userOptional).isPresent();

        User user = userOptional.get();

        assertThat(user).isEqualTo(validUsersList.get(userIndex));
    }

    @Test
    void authenticate_authenticateByNotExistingLogin_ShouldReturnEmptyOptional() throws DaoException, ServiceException {
        doReturn(Optional.empty()).when(userDAO).authenticate(anyString(), anyString());

        Optional<User> userOptional = userService.authenticate("randomLogin", "randompassword123");

        assertThat(userOptional).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidLogin!", "another_invalid_-+login"})
    void authenticate_authenticateInvalidLogin_ShouldReturnEmptyOptional(String login) throws ServiceException {

        Optional<User> userOptional = userService.authenticate(login, "randompassword123");

        assertThat(userOptional).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidPassword!", "", "another_invalid_-+login"})
    void authenticate_authenticateInvalidPassword_ShouldReturnEmptyOptional(String password) throws ServiceException {
        Optional<User> userOptional = userService.authenticate("validLogin", password);

        assertThat(userOptional).isEmpty();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/password_encryption.csv")
    void authenticate_whenDaoThrowsException_ShouldThrowServiceException(String password, String encryptedPassword) throws DaoException {
        doThrow(DaoException.class).when(userDAO).authenticate("login", encryptedPassword);

        assertThatThrownBy(() -> userService.authenticate("login", password)).isExactlyInstanceOf(ServiceException.class);
    }

    @Test
    void canBeRegistered_validUser_ShouldReturnEmptyValidationList() throws ServiceException, DaoException {
        doReturn(Optional.empty()).when(userDAO).findByPhone(usersToSave.get(0).getPhoneNumber());
        doReturn(Optional.empty()).when(userDAO).findByEmail(usersToSave.get(0).getEmail());
        doReturn(Optional.empty()).when(userDAO).findByLogin(usersToSave.get(0).getLogin());

        User userToSave = usersToSave.get(0);

        List<String> validationList = userService.canBeRegistered(userToSave);

        assertThat(validationList).isEmpty();
    }

    @Test
    void canBeRegistered_LoginEmailPhoneAlreadyExists_ShouldReturnLoginAlreadyExistsMsg() throws ServiceException, DaoException {
        doReturn(Optional.of(validUsersList.get(3))).when(userDAO).findByPhone(validUsersList.get(3).getPhoneNumber());
        doReturn(Optional.of(validUsersList.get(3))).when(userDAO).findByEmail(validUsersList.get(3).getEmail());
        doReturn(Optional.of(validUsersList.get(3))).when(userDAO).findByLogin(validUsersList.get(3).getLogin());

        User userToSave = validUsersList.get(3);

        var validationList = userService.canBeRegistered(userToSave);

        assertThat(validationList).containsExactly(UserParameters.USER_EMAIL_ALREADY_EXISTS,
                UserParameters.USER_LOGIN_ALREADY_EXISTS,
                UserParameters.USER_PHONE_ALREADY_EXISTS);
    }

    @Test
    void canBeRegistered_LoginAlreadyExists_ShouldReturnLoginAlreadyExistsMsg() throws ServiceException, DaoException {
        doReturn(Optional.empty()).when(userDAO).findByPhone(validUsersList.get(3).getPhoneNumber());
        doReturn(Optional.empty()).when(userDAO).findByEmail(validUsersList.get(3).getEmail());
        doReturn(Optional.of(validUsersList.get(3))).when(userDAO).findByLogin(validUsersList.get(3).getLogin());

        User userToSave = validUsersList.get(3);

        var validationList = userService.canBeRegistered(userToSave);

        assertThat(validationList).containsExactly(UserParameters.USER_LOGIN_ALREADY_EXISTS);
    }

    @Test
    void canBeRegistered_PhoneAlreadyExists_ShouldReturnLoginAlreadyExistsMsg() throws ServiceException, DaoException {
        doReturn(Optional.of(validUsersList.get(3))).when(userDAO).findByPhone(validUsersList.get(3).getPhoneNumber());
        doReturn(Optional.empty()).when(userDAO).findByEmail(validUsersList.get(3).getEmail());
        doReturn(Optional.empty()).when(userDAO).findByLogin(validUsersList.get(3).getLogin());

        User userToSave = validUsersList.get(3);

        var validationList = userService.canBeRegistered(userToSave);

        assertThat(validationList).containsExactly(UserParameters.USER_PHONE_ALREADY_EXISTS);
    }

    @Test
    void canBeRegistered_EmailAlreadyExists_ShouldReturnLoginAlreadyExistsMsg() throws ServiceException, DaoException {
        doReturn(Optional.empty()).when(userDAO).findByPhone(validUsersList.get(3).getPhoneNumber());
        doReturn(Optional.of(validUsersList.get(3))).when(userDAO).findByEmail(validUsersList.get(3).getEmail());
        doReturn(Optional.empty()).when(userDAO).findByLogin(validUsersList.get(3).getLogin());

        User userToSave = validUsersList.get(3);

        var validationList = userService.canBeRegistered(userToSave);

        assertThat(validationList).containsExactly(UserParameters.USER_EMAIL_ALREADY_EXISTS);
    }

    @Test
    void canBeRegistered_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(userDAO).findByEmail(anyString());

        assertThatThrownBy(() -> userService.canBeRegistered(usersToSave.get(0))).isExactlyInstanceOf(ServiceException.class);
    }


    @ParameterizedTest
    @CsvSource({"1, 4", "1 , 3", "1, 2", "1, 3"})
    void findAll_validRange_ShouldReturnUsersList(int start, int offset) throws DaoException, ServiceException {
        doReturn(validUsersList.subList(start - 1, offset - 1)).when(userDAO).findAll(start, offset);

        List<User> userList = userService.findAll(start, offset);

        assertThat(userList).containsExactly(validUsersList.subList(start - 1, offset - 1).toArray(User[]::new));
    }

    @ParameterizedTest
    @CsvSource({"-1, 4", "-2 , 3", "-3, 2", "-4, 3"})
    void findAll_startIsNegative_ShouldReturnEmptyList(int start, int offset) throws DaoException, ServiceException {
        doReturn(List.of()).when(userDAO).findAll(start, offset);

        List<User> userList = userService.findAll(start, offset);

        assertThat(userList).isEmpty();
    }

    @Test
    void findAll_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(userDAO).findAll(anyInt(), anyInt());

        assertThatThrownBy(() -> userService.findAll(1, 100)).isExactlyInstanceOf(ServiceException.class);
    }

    @Test
    void countTotalUsers_ShouldReturnUsersCount() throws DaoException, ServiceException {
        doReturn(validUsersList.size()).when(userDAO).countTotalUsers();

        int usersCount = userService.countTotalUsers();

        assertThat(usersCount).isEqualTo(validUsersList.size());
    }

    @Test
    void countTotalUsers_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(userDAO).countTotalUsers();

        assertThatThrownBy(() -> userService.countTotalUsers()).isExactlyInstanceOf(ServiceException.class);
    }


    @Test
    void blockUser_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException, ServiceException {
        doThrow(DaoException.class).when(userDAO).block(anyLong());

        assertThatThrownBy(() -> userService.blockUser(anyLong())).isExactlyInstanceOf(ServiceException.class);

        verify(userDAO, times(1)).block(anyLong());
    }

    @Test
    void blockUser_ifUserExists_ReturnTrue() throws DaoException, ServiceException {
        doReturn(false).when(userDAO).block(anyLong());

        assertThat(userService.blockUser(anyLong())).isFalse();

        verify(userDAO, times(1)).block(anyLong());
    }

    @Test
    void blockUser_ifUserDoesntExist_ReturnFalse() throws DaoException, ServiceException {
        doReturn(true).when(userDAO).block(anyLong());

        assertThat(userService.blockUser(anyLong())).isTrue();

        verify(userDAO, times(1)).block(anyLong());
    }

    @Test
    void unblockUser_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException, ServiceException {
        doThrow(DaoException.class).when(userDAO).unblock(anyLong());

        assertThatThrownBy(() -> userService.unblockUser(anyLong())).isExactlyInstanceOf(ServiceException.class);

        verify(userDAO, times(1)).unblock(anyLong());
    }

    @Test
    void unblockUser_ifUserExists_ReturnTrue() throws DaoException, ServiceException {
        doReturn(false).when(userDAO).unblock(anyLong());

        assertThat(userService.unblockUser(anyLong())).isFalse();

        verify(userDAO, times(1)).unblock(anyLong());
    }

    @Test
    void unblockUser_ifUserDoesntExist_ReturnFalse() throws DaoException, ServiceException {
        doReturn(true).when(userDAO).unblock(anyLong());

        assertThat(userService.unblockUser(anyLong())).isTrue();

        verify(userDAO, times(1)).unblock(anyLong());
    }

    @ParameterizedTest
    @CsvSource({"1, ADMIN", "2, USER", "3, LIBRARIAN"})
    void setUserRole(long userId, UserRole newUserRole) throws ServiceException, DaoException {
        userService.setUserRole(userId, newUserRole);

        verify(userDAO, times(1)).setUserRole(userId, newUserRole);
    }

    @ParameterizedTest
    @ValueSource(strings = {"USER", "ADMIN", "LIBRARIAN"})
    void setUserRole_whenDaoThrowsException_ShouldThrowServiceException(UserRole userRole) throws DaoException {

        doThrow(DaoException.class).when(userDAO).setUserRole(anyLong(), any());

        assertThatThrownBy(() -> userService.setUserRole(current().nextLong(), userRole)).isExactlyInstanceOf(ServiceException.class);

        verify(userDAO, times(1)).setUserRole(anyLong(), any());
    }

    @Test
    void findAllReaders_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException {

        doThrow(DaoException.class).when(userDAO).findAllReaders(anyInt(), anyInt());

        assertThatThrownBy(() -> userService.findAllReaders(current().nextInt(), current().nextInt())).isExactlyInstanceOf(ServiceException.class);

        verify(userDAO, times(1)).findAllReaders(anyInt(), anyInt());
    }

    @ParameterizedTest
    @CsvSource({"1, 2"})
    void findAllReaders_validRange_ShouldReturnReadersList(int start, int offset) throws DaoException, ServiceException {
        List<User> expectedReadersList = validUsersList.stream().filter(u -> u.getRole().equals(UserRole.USER)).toList();

        doReturn(expectedReadersList.subList(start - 1, offset - 1)).when(userDAO).findAllReaders(start, offset);

        var readersListActual = userService.findAllReaders(start, offset);

        assertThat(readersListActual).isEqualTo(expectedReadersList);

        verify(userDAO, times(1)).findAllReaders(start, offset);
    }

    @ParameterizedTest
    @CsvSource({"1, 4", "1 , 3", "1, 2", "1, 3"})
    void findAllReaders_validRange_ShouldReturnUsersList(int start, int offset) throws DaoException, ServiceException {
        doReturn(validUsersList.subList(start - 1, offset - 1)).when(userDAO).findAll(start, offset);

        List<User> userList = userService.findAll(start, offset);

        assertThat(userList).containsExactly(validUsersList.subList(start - 1, offset - 1).toArray(User[]::new));
    }

    @ParameterizedTest
    @CsvSource({"-1, 4", "-2 , 3", "-3, 2", "-4, 3"})
    void findAllReaders_startIsNegative_ShouldReturnEmptyList(int start, int offset) throws DaoException, ServiceException {
        doReturn(List.of()).when(userDAO).findAllReaders(start, offset);

        List<User> userList = userService.findAllReaders(start, offset);

        assertThat(userList).isEmpty();
    }

    @Test
    void countReaders_whenDaoThrowsException_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(userDAO).countReaders(anyBoolean());

        assertThatThrownBy(() -> userService.countReaders(anyBoolean())).isExactlyInstanceOf(ServiceException.class);

        verify(userDAO, times(1)).countReaders(anyBoolean());
    }
}