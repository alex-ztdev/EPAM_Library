package com.my.library.controller.command.impl;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request) {
        //FIXME: redirect to error page?
        return new CommandResult(Pages.MAIN_PAGE, CommandDirection.REDIRECT);
    }
}
