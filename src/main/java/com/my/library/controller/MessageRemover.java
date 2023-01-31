package com.my.library.controller;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import jakarta.servlet.http.HttpSession;

public class MessageRemover {
    public void removeMessages(HttpSession session) {
        session.removeAttribute(BookParameters.BOOK_INVALID_DATA);
        session.removeAttribute(BookParameters.BOOK_ALREADY_EXISTS);
        session.removeAttribute(BookParameters.SUCCESSFULLY_UPDATED);

        session.removeAttribute(Parameters.UPDATE_BOOK);
        session.removeAttribute(Parameters.ADD_BOOK);
    }
}
