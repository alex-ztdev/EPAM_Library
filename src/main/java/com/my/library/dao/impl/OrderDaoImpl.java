package com.my.library.dao.impl;

import com.my.library.dao.AbstractDao;
import com.my.library.dao.OrderDAO;
import com.my.library.dao.constants.columns.OrdersColumns;
import com.my.library.dao.constants.queries.OrderQueries;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl extends AbstractDao implements OrderDAO {
    private static final Logger logger = LogManager.getLogger();

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Order> find(long id) throws DaoException {
        Order order = null;
        try (var statement = connection.prepareStatement(OrderQueries.FIND_ORDER_BY_ID)) {

            statement.setLong(1, id);
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    order = buildOrder(rs);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return order == null ? Optional.empty() : Optional.of(order);
    }

    private Order buildOrder(ResultSet resultSet) throws DaoException {
        try {
            Order order = new Order();
            order.setOrderId(resultSet.getLong(OrdersColumns.ID));
            order.setUserId(resultSet.getLong(OrdersColumns.USER_ID));
            order.setBookId(resultSet.getLong(OrdersColumns.BOOK_ID));
            order.setOrderStartDate(resultSet.getObject(OrdersColumns.ORDER_START_DATE, LocalDateTime.class));
            order.setOrderEndDate(resultSet.getObject(OrdersColumns.SUBSCRIPTION_END_DATE, LocalDateTime.class));
            order.setReturnDate(resultSet.getObject(OrdersColumns.RETURN_DATE, LocalDateTime.class));
            order.setOnSubscription(resultSet.getBoolean(OrdersColumns.ON_SUBSCRIPTION));

            return order;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    @Override
    public List<Order> findAll(int start, int offset) throws DaoException {
        List<Order> orderList = new ArrayList<>();
        try (var statement = connection.prepareStatement(OrderQueries.FIND_ALL_ORDERS_PAGINATION)) {
            statement.setInt(1, start);
            statement.setInt(2, offset);

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    orderList.add(buildOrder(rs));
                }
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void save(Order order) throws DaoException {
        try (var statement = connection.prepareStatement(OrderQueries.INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            int k = 1;
            statement.setLong(k++, order.getUserId());
            statement.setLong(k++, order.getBookId());
            statement.setObject(k++, order.getOrderStartDate());
            statement.setObject(k++, order.getOrderEndDate());
            statement.setObject(k++, order.getReturnDate());
            statement.setBoolean(k, order.isOnSubscription());

            statement.executeUpdate();

            try (var keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setOrderId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Order order) throws DaoException {
        try (var statement = connection.prepareStatement(OrderQueries.UPDATE_ORDER)) {

            int k = 1;
            statement.setLong(k++, order.getUserId());
            statement.setLong(k++, order.getBookId());
            statement.setObject(k++, order.getOrderStartDate());
            statement.setObject(k++, order.getOrderEndDate());
            statement.setObject(k++, order.getReturnDate());
            statement.setBoolean(k++, order.isOnSubscription());

            statement.setLong(k, order.getOrderId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Order order) throws DaoException {
        try (var statement = connection.prepareStatement(OrderQueries.DELETE_ORDER)) {

            statement.setLong(1, order.getOrderId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> findAllUsersOrders(long userId, int start, int offset) throws DaoException {
        logger.log(Level.DEBUG, "OrderDaoImpl/findAllUsersOrders method invoked with parameters: user_id=" +
                userId + " start=" + start + " offset=" + offset);

        List<Order> orderList = new ArrayList<>();

        try (var statement = connection.prepareStatement(OrderQueries.FIND_ALL_USER_ORDERS_PAGINATION)) {
            int k = 1;
            statement.setLong(k++, userId);
            statement.setInt(k++, start);
            statement.setInt(k, offset);

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    orderList.add(buildOrder(rs));
                }
            }
            return orderList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countUserOrders(long userId) throws DaoException {
        try (var statement = connection.prepareStatement(OrderQueries.COUNT_USERS_ORDERS)) {
            statement.setLong(1, userId);

            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countTotalOrders() throws DaoException {
        try (var statement = connection.createStatement()) {
            try (var rs = statement.executeQuery(OrderQueries.COUNT_ALL_ORDERS)) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
