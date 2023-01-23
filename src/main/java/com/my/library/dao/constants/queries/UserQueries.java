package com.my.library.dao.constants.queries;

public interface UserQueries {
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
    String FIND_USER_BY_ID = FIND_ALL_USERS + "WHERE id=?";
    String INSERT_USER = """
            INSERT INTO Users
            (login, password, role_id, status_id, email, phone_number, first_name, second_name)
            VALUES(?,?,?,?,?,?,?,?)
            """;

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
    String CHANGE_USER_STATUS_USER = """
            UPDATE Users SET
            status_id = ?
            WHERE id = ?
            """;


    String AUTHENTICATE_BY_LOGIN_PASSWORD = FIND_ALL_USERS +"WHERE login=? AND password=?";
    String FIND_BY_LOGIN = FIND_ALL_USERS + "WHERE login=?";
    String FIND_BY_EMAIL = FIND_ALL_USERS + "WHERE email=?";
    String FIND_BY_PHONE = FIND_ALL_USERS + "WHERE phone=?";


}
