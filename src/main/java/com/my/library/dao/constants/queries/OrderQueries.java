package com.my.library.dao.constants.queries;

public interface OrderQueries {
    String FIND_ALL_ORDERS = """
            SELECT
            	id,
            	user_id,
            	book_id,
            	order_start_date,
            	order_end_date,
            	actual_return_date
            FROM Orders
            """;
    String FIND_ORDER_BY_ID = FIND_ALL_ORDERS + "WHERE id=?";
    String INSERT_ORDER = """
            INSERT INTO Orders (user_id, book_id, order_start_date, order_end_date, actual_return_date)
            VALUES(?,?,?,?,?)
            """;

    String UPDATE_ORDER = """
            UPDATE Orders
            SET
            user_id = ?,
            book_id = ?,
            order_start_date = ?,
            order_end_date = ?,
            actual_return_date=?
            WHERE id =?
            """;
    String DELETE_ORDER = """
            DELETE FROM Orders
            WHERE id = ?
            """;
    String FIND_ALL_USER_ORDERS = FIND_ALL_ORDERS + "WHERE user_id = ?";
}
