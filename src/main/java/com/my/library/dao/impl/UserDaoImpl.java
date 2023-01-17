package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.UserDAO;
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
                resultSet.getString(UsersColumns.SECOND_NAME),
                resultSet.getDate(UsersColumns.BIRTH_DATE).toLocalDate()
        );
    }

    @Override
    public List<User> findAll() throws DaoException{
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
    public void save(User user) throws DaoException{
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            statement.setString(k++,user.getLogin());
            statement.setString(k++,user.getPassword());
            statement.setLong(k++,user.getRole().ordinal()+1);
            statement.setLong(k++,user.getStatus().ordinal()+1);
            statement.setString(k++, user.getEmail());
            statement.setString(k++, user.getPhoneNumber());
            statement.setString(k++, user.getFirstName());
            statement.setString(k++, user.getSecondName());
            statement.setDate(k, Date.valueOf(user.getBirthDate()));


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
    public boolean update(User user) throws DaoException{
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(UserQueries.UPDATE_USER)) {
            int k = 1;
            statement.setString(k++,user.getLogin());
            statement.setString(k++,user.getPassword());
            statement.setLong(k++,user.getRole().ordinal()+1);
            statement.setLong(k++,user.getStatus().ordinal()+1);
            statement.setString(k++, user.getEmail());
            statement.setString(k++, user.getPhoneNumber());
            statement.setString(k++, user.getFirstName());
            statement.setString(k++, user.getSecondName());
            statement.setDate(k++, Date.valueOf(user.getBirthDate()));
            statement.setLong(k, user.getUserId());

            var res = statement.executeUpdate();
            return res == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void block(User user) throws DaoException{

    }
}
