package com.my.library.controller.command.constant;

public interface UserConstants {
    String CONTENT_FROM_RESOURCES = "This string will be replaced with data from property file";
    String INVALID_LOGIN_PASSWORD = "invalidLoginPassword";
    String USER_IS_BLOCKED = "isBlocked";
    String USER_IN_SESSION = "user";


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