package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.services.BookService;
import com.my.library.utils.Pages;
import com.my.library.utils.builder.RequestBookBuilder;
import jakarta.servlet.http.HttpServletRequest;

public class UpdateBookCommand implements Command {
    private final BookService bookService;


    public UpdateBookCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        var bookContainer = new RequestBookBuilder().buildBookForUpdate(request);

        if (bookContainer.isEmpty()) {
            request.getSession().setAttribute(BookParameters.BOOK_INVALID_DATA, BookParameters.BOOK_INVALID_DATA);
        } else {

        }
        return new CommandResult(Pages.BOOK_EDIT, CommandDirection.REDIRECT);
    }
}
