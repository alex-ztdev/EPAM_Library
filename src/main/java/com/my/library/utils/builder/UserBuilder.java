package com.my.library.utils.builder;

import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.columns.UsersColumns;
import com.my.library.entities.User;
import jakarta.servlet.http.HttpServletRequest;

public class UserBuilder {
    //TODO: implement buildUserForUpdate
    public User buildUserForUpdate(HttpServletRequest request) {
        String id = request.getParameter(UsersColumns.ID);
        String name = request.getParameter(UsersColumns.FIRST_NAME);
        String lastName = request.getParameter(UsersColumns.SECOND_NAME);
        String email = request.getParameter(UsersColumns.EMAIL);
        String login = request.getParameter(UsersColumns.LOGIN);
        String phoneNumber = request.getParameter(UsersColumns.PHONE_NUMBER);
        //UserRole role = request.getParameter(UsersColumns.ROLE_ID); //TODO: implement rolesFactory
//        String blocked = request.getParameter(UsersColumns.SY);
//        String blocked = request.getParameter(UsersColumns.BIRTH_DATE);

//        return new User(Long.parseLong(id), name, lastName, email, login, role, Boolean.parseBoolean(blocked.trim()));
        return null;
    }

}
