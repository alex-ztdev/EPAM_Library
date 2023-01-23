package com.my.library.controller.command.constant;

public interface UserConstants {
    String CONTENT_FROM_RESOURCES = "This string will be replaced with data from property file";
    String INVALID_LOGIN_PASSWORD = "invalidLoginPassword";
    String REG_INVALID_LOGIN = "regInvalidLogin";
    String REG_INVALID_PASSWORD = "regInvalidPassword";
    String REG_INVALID_EMAIL = "regInvalidEmail";
    String REG_INVALID_PHONE = "regInvalidPhone";
    String REG_INVALID_NAME = "regInvalidName";
    String REG_FORM = "regForm";
    String USER_IS_BLOCKED = "isBlocked";
    String USER_IN_SESSION = "user";

    String USER_EMAIL_ALREADY_EXISTS = "emailAlreadyExists";
    String USER_LOGIN_ALREADY_EXISTS = "loginAlreadyExists";
    String USER_PHONE_ALREADY_EXISTS = "phoneAlreadyExists";


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
