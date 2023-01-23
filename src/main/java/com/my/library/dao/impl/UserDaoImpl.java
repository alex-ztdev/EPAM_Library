package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.UserDAO;
import com.my.library.dao.constants.Constraints;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.dao.constants.queries.UserQueries;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class UserDaoImpl implements UserDAO {
    private static final ConnectionPool dbm = ConnectionPool.getInstance();
    private static volatile UserDaoImpl INSTANCE;

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        UserDaoImpl instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (UserDaoImpl.class) {
            if (instance == null) {
                instance = new UserDaoImpl();
            }
            return instance;
        }
    }

    @Override
    public Optional<User> find(long id) throws DaoException {
        User user = null;
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.FIND_USER_BY_ID)) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = buildUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong(UsersColumns.ID),
                resultSet.getString(UsersColumns.LOGIN),
                resultSet.getString(UsersColumns.PASSWORD),
                UserRole.values()[resultSet.getInt(UsersColumns.ROLE_ID) - 1],
                UserStatus.values()[resultSet.getInt(UsersColumns.STATUS_ID) - 1],
                resultSet.getString(UsersColumns.EMAIL),
                resultSet.getString(UsersColumns.PHONE_NUMBER),
                resultSet.getString(UsersColumns.FIRST_NAME),
                resultSet.getString(UsersColumns.SECOND_NAME)
        );
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> userList = new ArrayList<>();

        try (var connection = dbm.get();
             var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(UserQueries.FIND_ALL_USERS)) {
                while (rs.next()) {
                    userList.add(buildUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return userList;
    }

    @Override
    public void save(User user) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            statement.setString(k++, user.getLogin());
            statement.setString(k++, user.getPassword());
            statement.setLong(k++, user.getRole().ordinal() + 1);
            statement.setLong(k++, user.getStatus().ordinal() + 1);
            statement.setString(k++, user.getEmail());
            statement.setString(k++, user.getPhoneNumber());
            statement.setString(k++, user.getFirstName());
            statement.setString(k, user.getSecondName());

            statement.executeUpdate();

            try (var genKey = statement.getGeneratedKeys()) {
                if (genKey.next()) {
                    user.setUserId(genKey.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(User user) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.UPDATE_USER)) {
            int k = 1;
            statement.setString(k++, user.getLogin());
            statement.setString(k++, user.getPassword());
            statement.setLong(k++, user.getRole().ordinal() + 1);
            statement.setLong(k++, user.getStatus().ordinal() + 1);
            statement.setString(k++, user.getEmail());
            statement.setString(k++, user.getPhoneNumber());
            statement.setString(k++, user.getFirstName());
            statement.setString(k++, user.getSecondName());
            statement.setLong(k, user.getUserId());

            var res = statement.executeUpdate();
            return res == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void block(User user) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.CHANGE_USER_STATUS_USER)) {

            statement.setLong(1, UserStatus.BLOCKED.ordinal() + 1);
            statement.setLong(2, user.getUserId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void unblock(User user) throws DaoException {
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.CHANGE_USER_STATUS_USER)) {

            statement.setLong(1, UserStatus.NORMAL.ordinal() + 1);
            statement.setLong(2, user.getUserId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws DaoException {
        User res = null;
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.AUTHENTICATE_BY_LOGIN_PASSWORD)) {
            statement.setString(1, login);
            statement.setString(2, password);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    res = buildUser(rs);
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return res == null ? Optional.empty() : Optional.of(res);
    }

    @Override
    public Optional<User> findByLogin(String login) throws DaoException {
        User user = null;
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.FIND_BY_LOGIN)) {

            statement.setString(1, login);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = buildUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) throws DaoException {
        User user = null;
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.FIND_BY_EMAIL)) {

            statement.setString(1, email);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = buildUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public Optional<User> findByPhone(String phone) throws DaoException {
        User user = null;
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.FIND_BY_PHONE)) {

            statement.setString(1, phone);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = buildUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }

//    String msg = "User with such ";
//    String defaultMsg = msg;
//
//            if (e.getMessage().contains(Constraints.UQ_USER_LOGIN)) {
//        msg += "login ";
//    }
//            if (e.getMessage().contains(Constraints.UQ_USER_EMAIL)) {
//        msg += "email ";
//    }
//            if (e.getMessage().contains(Constraints.UQ_USER_PHONE_NUMBER)) {
//        msg += "phone number ";
//    }
//    String s = defaultMsg.equals(msg) ? "Error in UserDao while saving user" : msg + "already exists";


}
