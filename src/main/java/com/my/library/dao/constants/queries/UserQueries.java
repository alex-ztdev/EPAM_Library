package com.my.library.dao.constants.queries;

public interface UserQueries {
    String FIND_ALL_USERS = """
            SELECT id,
                login,
                password,
                role_id,
                status_id,
                email,
                phone_number,
                first_name,
                second_name,
                birth_date
            FROM Users
            """;
    String FIND_USER_BY_ID = FIND_ALL_USERS + "WHERE id=?";
    String INSERT_USER = """
            INSERT INTO Users
            (login, password, role_id, status_id, email, phone_number, first_name, second_name, birth_date)
            VALUES(?,?,?,?,?,?,?,?,?)
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
                second_name = ?,
                birth_date = ?
            WHERE id = ?
            """;
    String CHANGE_USER_STATUS_USER = """
            UPDATE Users SET
            status_id = ?
            WHERE id = ?
            """;


    String AUTHENTICATE_BY_LOGIN_PASSWORD = FIND_ALL_USERS +"WHERE login=? AND password=?";
}
