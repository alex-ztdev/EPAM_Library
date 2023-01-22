package com.my.library.controller.command.impl;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request) {
        //TODO: implement execute method
        return null;
    }
}
