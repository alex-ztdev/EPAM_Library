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
            	return_date,
            	on_subscription,
            	status
            FROM Orders
            """;
    //language=TSQL
    String FIND_ORDER_BY_ID = FIND_ALL_ORDERS + "WHERE id=?";
    //language=TSQL
    String INSERT_ORDER = """
            INSERT INTO Orders (user_id, book_id, order_start_date, order_end_date, return_date, on_subscription, status)
            VALUES(?,?,?,?,?,?,?)
            """;
    //language=TSQL
    String UPDATE_ORDER = """
            UPDATE Orders
            SET
            user_id = ?,
            book_id = ?,
            order_start_date = ?,
            order_end_date = ?,
            return_date=?,
            on_subscription=?,
            status=?
            WHERE id =?
            """;
    //language=TSQL
    String DELETE_ORDER = """
            DELETE FROM Orders
            WHERE id = ?
            """;
    //language=TSQL
    String FIND_ALL_USER_ORDERS = FIND_ALL_ORDERS + "WHERE user_id = ?";

    //language=TSQL
    String FIND_ALL_USER_ORDERS_PAGINATION = FIND_ALL_ORDERS + """
            WHERE user_id = ?
            ORDER BY  return_date, order_end_date
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;

    //language=TSQL
    String FIND_ALL_ORDERS_PAGINATION = FIND_ALL_ORDERS + """
            ORDER BY  return_date, order_end_date
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;

    //language=TSQL
    String COUNT_ALL_ORDERS = """
            SELECT COUNT(id) FROM Orders
            """;
    //language=TSQL
    String COUNT_USERS_ORDERS = COUNT_ALL_ORDERS + " WHERE user_id = ?";

    //language=TSQL
    String SET_RETURN_DATE = """
            UPDATE Orders SET
            return_date=?
            WHERE id=?
            """;

}
