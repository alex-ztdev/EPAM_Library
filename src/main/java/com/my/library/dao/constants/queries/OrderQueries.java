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
}
