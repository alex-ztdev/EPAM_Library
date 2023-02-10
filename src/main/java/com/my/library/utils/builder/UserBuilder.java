package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.entities.User;
import jakarta.servlet.http.HttpServletRequest;

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

        if (login.isEmpty() || email.isEmpty() || firstName.isEmpty() || secondName.isEmpty() || password.isEmpty() || confPassword.isEmpty() || !password.equals(confPassword)) {
            return Optional.empty();
        }
        return Optional.of(new User(login, password, UserRole.USER, UserStatus.NORMAL, email, phoneNumber.isEmpty() ? null : phoneNumber, firstName, secondName));
    }
}