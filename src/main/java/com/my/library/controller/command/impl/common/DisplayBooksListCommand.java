package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;

public class DisplayBooksListCommand implements Command {
    private static final int RECORDS_PER_PAGE = 10;

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        if (request.getParameter(Parameters.BOOKS_LIST_CURR_PAGE) != null) {

        }
        return new CommandResult(Pages.BOOKS_LIST, CommandDirection.FORWARD);
    }
}
