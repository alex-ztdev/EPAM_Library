package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class DefaultCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.UNSUPPORTED_OPERATION);

        return new CommandResult(RedirectToPage.UNSUPPORTED_OPERATION, CommandDirection.REDIRECT);
    }

}
