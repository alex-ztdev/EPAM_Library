package com.my.library.dao.constants.queries;

public interface OrderQueries {
    //language=TSQL
    String FIND_ALL_ORDERS = """
            SELECT
            	Orders.id,
            	Orders.user_id,
            	Orders.book_id,
            	Orders.order_start_date,
            	Orders.order_end_date,
            	Orders.return_date,
            	Orders.on_subscription,
            	Orders.status
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
            Orders.user_id = ?,
            Orders.book_id = ?,
            Orders.order_start_date = ?,
            Orders.order_end_date = ?,
            Orders.return_date=?,
            Orders.on_subscription=?,
            Orders.status=?
            WHERE id =?
            """;
    //language=TSQL
    String DELETE_ORDER = """
            DELETE FROM Orders
            WHERE id = ?
            """;
    //language=TSQL
    String FIND_ALL_USER_ORDERS = FIND_ALL_ORDERS + """
            INNER JOIN Users U on U.id = Orders.user_id
            WHERE user_id = ? and  U.status_id = 1
            """;

    //language=TSQL
    String FIND_ALL_USER_ORDERS_PAGINATION = FIND_ALL_ORDERS + """
            INNER JOIN Users U on U.id = Orders.user_id
            WHERE user_id = ? and  U.status_id !=2 and status in (?,?,?)
            ORDER BY  return_date, order_end_date
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;


    //language=TSQL
    String FIND_ALL_ORDERS_PAGINATION = FIND_ALL_ORDERS + """
            INNER JOIN Users U on U.id = Orders.user_id
            WHERE U.status_id !=2
            ORDER BY return_date, order_end_date
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;

    //language=TSQL
    String COUNT_ALL_ORDERS = """
            SELECT COUNT(*) FROM Orders
            INNER JOIN Users U on U.id = Orders.user_id
            WHERE U.status_id !=2
            """;
    //language=TSQL
    String COUNT_USERS_ORDERS = COUNT_ALL_ORDERS + "and user_id = ?";

    //language=TSQL
    String COUNT_USERS_ORDERS_BY_STATUSES = """
            SELECT COUNT(*) FROM Orders
            INNER JOIN Users U on U.id = Orders.user_id
            WHERE U.status_id !=2 and user_id = ? and status in (?,?,?)
            """;


    //language=TSQL
    String SET_RETURN_DATE = """
            UPDATE Orders SET
            return_date=?
            WHERE id=?
            """;
    //language=TSQL
    String CHANGE_STATUS = """
            UPDATE Orders SET
            Orders.status = ?
            WHERE id = ?
            """;

    //language=TSQL
    String FIND_ORDERS_BY_STATUS_PAGINATION = FIND_ALL_ORDERS + """
            INNER JOIN Users U on U.id = Orders.user_id
            WHERE U.status_id !=2 and Orders.status in (?, ? ,?)
            ORDER BY status, return_date, order_end_date
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;

    //language=TSQL
    String COUNT_ORDERS_BY_STATUS = """
            SELECT COUNT(*) FROM Orders
            INNER JOIN Users U on U.id = Orders.user_id
            WHERE U.status_id != 2 and Orders.status in (?, ? ,?)
            """;

}
