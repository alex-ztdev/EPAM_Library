package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.entities.User;
import com.my.library.utils.validator.constants.UserRegex;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserValidator {
    public List<String> validateUserParameters(HttpServletRequest request) {

        List<String> validationList = new ArrayList<>();
        String firstName = request.getParameter(UsersColumns.FIRST_NAME);
        String lastName = request.getParameter(UsersColumns.SECOND_NAME);
        String email = request.getParameter(UsersColumns.EMAIL);
        String login = request.getParameter(UsersColumns.LOGIN);
        String password = request.getParameter(UsersColumns.PASSWORD);

//        if(firstName.isEmpty() || !isValidName(firstName)){
//            validationList.add(DiffConstant.NAME_ERROR);
//        }
//
//        if(lastName.isEmpty() || !isValidName(lastName)){
//            validationList.add(DiffConstant.LAST_NAME_ERROR);
//        }
//
//        if(email.isEmpty() || !isValidEmail(email)){
//            validationList.add(DiffConstant.EMAIL_ERROR);
//        }
//
//        if(login.isEmpty() || !isValidLogin(login)){
//            validationList.add(DiffConstant.LOGIN_ERROR);
//        }
//
//        if(password != null){ // Not null means new user, null means old user just we update his is data.
//            if(password.isEmpty() || !isValidPassword(password)){
//                validationList.add(DiffConstant.PASSWORD_ERROR);
//            }
//        }

        return validationList;
    }

    public List<String> validateUserParameters(User user) {
        List<String> validationList = new ArrayList<>();

        String login = user.getLogin();
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        String firstName = user.getFirstName();
        String secondName = user.getSecondName();
        String password = user.getPassword();

        if (!isValidLogin(login)) {
            validationList.add(UserParameters.REG_INVALID_LOGIN);
        }
        if (!isValidLogin(password)) {
            validationList.add(UserParameters.REG_INVALID_PASSWORD);
        }
        if (!isValidEmail(email)) {
            validationList.add(UserParameters.REG_INVALID_EMAIL);
        }
        if (phoneNumber != null && !phoneNumber.isEmpty() && !isValidPhone(phoneNumber)) {
            System.out.println(phoneNumber);
            validationList.add(UserParameters.REG_INVALID_PHONE);
        }
        if (!isValidName(firstName) || !isValidName(secondName)) { //FIXME: split into first and sec
            validationList.add(UserParameters.REG_INVALID_NAME);
        }
        return validationList;
    }

    private static boolean isValidPhone(String phone) {
        return Pattern.matches(UserRegex.PHONE.getRegex(), phone);
    }

    public static boolean isValidLogin(String login) {
        if (login == null || login.isEmpty()) return false;
        return Pattern.matches(UserRegex.LOGIN.getRegex(), login);
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) return false;
        return Pattern.matches(UserRegex.PASSWORD.getRegex(), password);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return Pattern.matches(UserRegex.EMAIL.getRegex(), email);
    }

    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        return Pattern.matches(UserRegex.NAME.getRegex(), name);
    }

    public static void setParameters(HttpServletRequest request, List<String> validationList) {
        if (validationList.contains(UserParameters.USER_EMAIL_ALREADY_EXISTS)) {
            request.setAttribute(UserParameters.USER_EMAIL_ALREADY_EXISTS, UserParameters.USER_EMAIL_ALREADY_EXISTS);
        }
        if (validationList.contains(UserParameters.USER_LOGIN_ALREADY_EXISTS)) {
            request.setAttribute(UserParameters.USER_LOGIN_ALREADY_EXISTS, UserParameters.USER_LOGIN_ALREADY_EXISTS);
        }
        if (validationList.contains(UserParameters.USER_PHONE_ALREADY_EXISTS)) {
            request.setAttribute(UserParameters.USER_PHONE_ALREADY_EXISTS, UserParameters.USER_PHONE_ALREADY_EXISTS);
        }
    }
}