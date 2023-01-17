package com.my.library.dao.constants.queries;

public interface UserQueries {
    String FIND_USER_BY_ID = """
            SELECT id, login, password, role_id, status_id, email, phone_number, first_name, second_name, birth_date
            FROM Users WHERE id=?
            """;

}
