package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UnsupportedOperationCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);

        return new CommandResult(Pages.UNSUPPORTED_COMMAND);
    }
}
