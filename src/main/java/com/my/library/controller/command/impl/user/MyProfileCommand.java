package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dto.UserDTO;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyProfileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.MY_PROFILE);

        var userDTO = (UserDTO) session.getAttribute(UserParameters.USER_IN_SESSION);

        logger.log(Level.DEBUG, "MyProfileCommand invoked for user: %s".formatted(userDTO.getUserId()));

        request.setAttribute(Parameters.USER_DTO, userDTO);

        return new CommandResult(Pages.MY_PROFILE);
    }
}
