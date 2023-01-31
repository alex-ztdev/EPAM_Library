package com.my.library.dao.constants.queries;

public interface OrderQueries {
    //language=TSQL
    String FIND_ALL_ORDERS = """
            SELECT
            	id,
            	user_id,
            	book_id,
            	order_start_date,
            	order_end_date,
            	is_returned,
            	on_subscription
            FROM Orders
            """;
    //language=TSQL
    String FIND_ORDER_BY_ID = FIND_ALL_ORDERS + "WHERE id=?";
    //language=TSQL
    String INSERT_ORDER = """
            INSERT INTO Orders (user_id, book_id, order_start_date, order_end_date, is_returned, on_subscription)
            VALUES(?,?,?,?,?,?)
            """;
    //language=TSQL
    String UPDATE_ORDER = """
            UPDATE Orders
            SET
            user_id = ?,
            book_id = ?,
            order_start_date = ?,
            order_end_date = ?,
            is_returned=?,
            on_subscription=?
            WHERE id =?
            """;
    //language=TSQL
    String DELETE_ORDER = """
            DELETE FROM Orders
            WHERE id = ?
            """;
    //language=TSQL
    String FIND_ALL_USER_ORDERS = FIND_ALL_ORDERS + "WHERE user_id = ?";
}
