package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.entities.User;
import com.my.library.utils.validator.constants.UserRegex;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserValidator {
    
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
        if (!isValidPassword(password)) {
            validationList.add(UserParameters.REG_INVALID_PASSWORD);
        }
        if (!isValidEmail(email)) {
            validationList.add(UserParameters.REG_INVALID_EMAIL);
        }
        if (phoneNumber != null && !phoneNumber.isEmpty() && !isValidPhone(phoneNumber)) {
            System.out.println(phoneNumber);
            validationList.add(UserParameters.REG_INVALID_PHONE);
        }
        if (!isValidName(firstName) || !isValidName(secondName)) {
            validationList.add(UserParameters.REG_INVALID_NAME);
        }
        return validationList;
    }

    public boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        return Pattern.matches(UserRegex.PHONE.getRegex(), phone);
    }

    public boolean isValidLogin(String login) {
        if (login == null || login.isEmpty()) return false;
        return Pattern.matches(UserRegex.LOGIN.getRegex(), login);
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) return false;
        return Pattern.matches(UserRegex.PASSWORD.getRegex(), password);
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return Pattern.matches(UserRegex.EMAIL.getRegex(), email);
    }

    public boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        return Pattern.matches(UserRegex.NAME.getRegex(), name);
    }


}