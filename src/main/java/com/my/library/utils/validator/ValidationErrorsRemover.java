package com.my.library.utils.validator;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import jakarta.servlet.http.HttpSession;

public class ValidationErrorsRemover {
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
}
