package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import com.my.library.utils.builder.BookBuilder;
import jakarta.servlet.http.HttpServletRequest;

public class UpdateBookCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        new BookBuilder().buildNewBook(request);
        return new CommandResult(Pages.BOOK_EDIT, CommandDirection.REDIRECT);
    }
}
