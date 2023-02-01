package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;

public class NotAuthorizedCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        return new CommandResult(Pages.NOT_AUTHORIZED);
    }
}
