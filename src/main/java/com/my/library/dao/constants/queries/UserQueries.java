package com.my.library.dao.constants.queries;

public interface UserQueries {
    //language=TSQL
    String FIND_ALL_USERS = """
            SELECT
                id,
                login,
                password,
                role_id,
                status_id,
                email,
                phone_number,
                first_name,
                second_name
            FROM Users
            """;
    //language=TSQL
    String FIND_USER_BY_ID = FIND_ALL_USERS + "WHERE id=?";
    //language=TSQL
    String INSERT_USER = """
            INSERT INTO Users
            (login, password, role_id, status_id, email, phone_number, first_name, second_name)
            VALUES(?,?,?,?,?,?,?,?)
            """;
    //language=TSQL
    String UPDATE_USER = """
            UPDATE Users SET
                login = ?,
                password = ?,
                role_id = ?,
                status_id = ?,
                email = ?,
                phone_number = ?,
                first_name = ?,
                second_name = ?
            WHERE id = ?
            """;
    //language=TSQL
    String CHANGE_USER_STATUS_USER = """
            UPDATE Users SET
            status_id = ?
            WHERE id = ?
            """;
    //language=TSQL
    String AUTHENTICATE_BY_LOGIN_PASSWORD = FIND_ALL_USERS + "WHERE login=? AND password=?";

    //language=TSQL
    String FIND_BY_LOGIN = FIND_ALL_USERS + "WHERE login=?";
    //language=TSQL
    String FIND_BY_EMAIL = FIND_ALL_USERS + "WHERE email=?";
    //language=TSQL
    String FIND_BY_PHONE = FIND_ALL_USERS + "WHERE phone_number=?";

    //language=TSQL
    String FIND_ALL_USERS_PAGINATION = FIND_ALL_USERS+
            """
            ORDER BY id
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;

    //language=TSQL
    String COUNT_ALL_USERS = """
            SELECT COUNT(id) FROM USERS
            """;
    //language=TSQL
    String SET_USER_ROLE = """
             UPDATE Users SET 
             role_id=?
             WHERE id =?
            """;
    //language=TSQL
    String FIND_READERS_PAGINATION = FIND_ALL_USERS + """
            WHERE role_id = 1
            ORDER BY id
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;
    //language=TSQL
    String COUNT_ALL_READERS = "SELECT COUNT(id) FROM USERS WHERE role_id=1";

    //language=TSQL
    String COUNT_UNBLOCKED_READERS = "SELECT COUNT(id) FROM USERS WHERE role_id=1 AND status_id=1";
}
