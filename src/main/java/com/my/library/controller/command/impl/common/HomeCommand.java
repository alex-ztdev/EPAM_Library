package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import com.my.library.utils.validator.MessagesRemover;
import jakarta.servlet.http.HttpServletRequest;

public class HomeCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var session = request.getSession();
        new MessagesRemover().removeLoginErrors(session);
        session.setAttribute(Parameters.PREVIOUS_PAGE, Pages.MAIN_PAGE);
        return new CommandResult(Pages.MAIN_PAGE);
    }
}
