package com.my.library.dao.impl;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.OrderDAO;
import com.my.library.dao.constants.columns.OrdersColumns;
import com.my.library.dao.constants.queries.OrderQueries;
import com.my.library.entities.Book;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDAO {
    private static final ConnectionPool dbm = ConnectionPool.getInstance();
    private static volatile OrderDaoImpl INSTANCE;

    private OrderDaoImpl() {
    }

    public static OrderDaoImpl getInstance() {
        OrderDaoImpl instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (OrderDaoImpl.class) {
            if (instance == null) {
                instance = new OrderDaoImpl();
            }
            return instance;
        }
    }

    @Override
    public Optional<Order> find(long id) throws DaoException {
        Order order = null;
        try (var connection = dbm.get();
             var statement = connection.prepareStatement(OrderQueries.FIND_ORDER_BY_ID)) {

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
            BookDaoImpl bookDao = BookDaoImpl.getInstance();
            UserDaoImpl userDao = UserDaoImpl.getInstance();

            Order order = new Order();

            User userInOrder = userDao.find(resultSet.getLong(OrdersColumns.USER_ID)).get();
            Book bookInOrder = bookDao.find(resultSet.getLong(OrdersColumns.BOOK_ID)).get();

            order.setOrderId(resultSet.getLong(OrdersColumns.ID));
            order.setUser(userInOrder);
            order.setBook(bookInOrder);
            order.setOrderStartDate(resultSet.getObject(OrdersColumns.ORDER_START_DATE, LocalDateTime.class));
            order.setOrderEndDate(resultSet.getObject(OrdersColumns.SUBSCRIPTION_END_DATE, LocalDateTime.class));
            order.setActualReturnDate(resultSet.getObject(OrdersColumns.ACTUAL_RETURN_DATE, LocalDateTime.class));

            return order;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> findAll() throws DaoException {
        List<Order> orderList = new ArrayList<>();
        try (var connection = dbm.get();
             var statement = connection.createStatement()) {

            try (var rs = statement.executeQuery(OrderQueries.FIND_ALL_ORDERS)) {
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

    }

    @Override
    public boolean update(Order order) throws DaoException {
        return false;
    }

    @Override
    public void delete(Order order) throws DaoException {

    }
}
