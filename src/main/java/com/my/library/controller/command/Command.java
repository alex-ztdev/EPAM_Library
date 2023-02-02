package com.my.library.controller.command;

import com.my.library.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    CommandResult execute(HttpServletRequest request) throws CommandException;
}
