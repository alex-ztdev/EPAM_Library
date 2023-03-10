package com.my.library.controller.command.impl.common;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dto.UserDTO;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        UserDTO userInSession = (UserDTO) session.getAttribute(UserParameters.USER_IN_SESSION);

        logger.log(Level.DEBUG, "User logged out: " + (userInSession == null ? "unknown" : userInSession.getUserId()));

        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.LOGIN_PAGE);
        session.removeAttribute(UserParameters.USER_IN_SESSION);

        return new CommandResult(Pages.LOGIN_PAGE);
    }
}
