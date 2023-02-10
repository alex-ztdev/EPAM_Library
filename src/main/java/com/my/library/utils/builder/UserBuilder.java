package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.entities.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;
import java.util.Optional;

public class UserBuilder {
    public Optional<User> buildNewUser(HttpServletRequest request) {
        String login = request.getParameter(UserParameters.REG_LOGIN);
        String email = request.getParameter(UserParameters.REG_EMAIL);
        String phoneNumber = request.getParameter(UserParameters.REG_PHONE);

        String firstName = request.getParameter(UserParameters.REG_FIRST_NAME);
        String secondName = request.getParameter(UserParameters.REG_SECOND_NAME);
        String password = request.getParameter(UserParameters.REG_PASSWORD);
        String confPassword = request.getParameter(UserParameters.REG_CONF_PASSWORD);

        if (containsNullOrBlankStringCheck(login, email, firstName, secondName, password, confPassword) || !password.equals(confPassword)) {
            return Optional.empty();
        }

        return Optional.of(new User(login, password, UserRole.USER, UserStatus.NORMAL, email, (phoneNumber==null || phoneNumber.isEmpty()) ? null : phoneNumber, firstName, secondName));
    }

    private boolean containsNullOrBlankStringCheck(String... strings) {
        if (strings == null) {
            return true;
        }
        for (String string : strings) {
            if (string == null || string.isBlank()) {
                return true;
            }
        }
        return false;
    }
}