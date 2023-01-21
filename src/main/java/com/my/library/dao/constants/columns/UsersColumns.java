package com.my.library.dao.constants.columns;

public interface UsersColumns {
    String ID = "id";
    String LOGIN = "login";
    String PASSWORD = "password";
    String ROLE_ID = "role_id";
    String STATUS_ID = "status_id"; //FIXME: make status only blocked or not
    String EMAIL = "email";
    String PHONE_NUMBER = "phone_number";
    String FIRST_NAME = "first_name";
    String SECOND_NAME = "second_name";
    String BIRTH_DATE = "birth_date"; //FIXME: remove completely
}
