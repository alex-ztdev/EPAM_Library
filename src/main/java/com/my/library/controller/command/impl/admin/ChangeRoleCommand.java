package com.my.library.controller.command.impl.admin;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.exceptions.CommandException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import com.my.library.utils.LongParser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeRoleCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public ChangeRoleCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "ChangeRoleCommand invoked");
        var newRoleStr = request.getParameter(UserParameters.ROLE);
        var userIdStr = request.getParameter(UserParameters.ROLE);


        var userIdContainer = new LongParser().parseLong(userIdStr);
        if (userIdContainer.isEmpty()) {
            logger.log(Level.DEBUG, "user id is null or blank. Redirect to error page");
            return new CommandResult(Pages.UNSUPPORTED_COMMAND, CommandDirection.REDIRECT);
        }
        long userId = userIdContainer.get();

//        if(user)



        return null;
    }
}
