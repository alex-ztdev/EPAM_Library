package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public class AddBookRedirectCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        return null;
    }
}