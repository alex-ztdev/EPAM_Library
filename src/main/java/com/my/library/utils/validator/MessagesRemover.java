package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import jakarta.servlet.http.HttpSession;

public class MessagesRemover {
    public void removeLoginErrors(HttpSession session) {
        session.removeAttribute(UserParameters.INVALID_LOGIN_PASSWORD);
        session.removeAttribute(UserParameters.USER_IS_BLOCKED);
    }

    public void removeBookErrors(HttpSession session) {
        session.removeAttribute(BookParameters.BOOK_INVALID_DATA);
        session.removeAttribute(BookParameters.BOOK_ALREADY_EXISTS);
        session.removeAttribute(BookParameters.SUCCESSFULLY_UPDATED);
        session.removeAttribute(BookParameters.SUCCESSFULLY_ADDED);

        session.removeAttribute(Parameters.UPDATE_BOOK);
        session.removeAttribute(Parameters.ADD_BOOK);
    }
    public void removeRegistrationErrors(HttpSession session) {
        session.removeAttribute(UserParameters.USER_EMAIL_ALREADY_EXISTS);
        session.removeAttribute(UserParameters.USER_LOGIN_ALREADY_EXISTS);
        session.removeAttribute(UserParameters.USER_PHONE_ALREADY_EXISTS);

        session.removeAttribute(UserParameters.REG_LOGIN_VAL);
        session.removeAttribute(UserParameters.REG_EMAIL_VAL);
        session.removeAttribute(UserParameters.REG_PHONE_VAL);
        session.removeAttribute(UserParameters.REG_FIRST_NAME_VAL);
        session.removeAttribute(UserParameters.REG_SECOND_NAME_VAL);
    }
}
