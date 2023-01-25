package com.my.library.controller.command.constant;

public interface UserParameters {
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

    String VALIDATION_LIST = "validationList";
    String USER_EMAIL_ALREADY_EXISTS = "emailAlreadyExists";
    String USER_LOGIN_ALREADY_EXISTS = "loginAlreadyExists";
    String USER_PHONE_ALREADY_EXISTS = "phoneAlreadyExists";


    String ID = "id";
    String LOGIN = "login";
    String PASSWORD = "password";

    String REG_LOGIN = "regLogin";
    String REG_EMAIL = "regEmail";
    String REG_PASSWORD = "regPassword";
    String REG_CONF_PASSWORD = "regConfirmPassword";
    String REG_PHONE = "regPhone";
    String REG_FIRST_NAME = "regFirstName";
    String REG_SECOND_NAME = "regSecondName";

}
