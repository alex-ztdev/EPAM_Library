package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.dto.UserDTO;
import com.my.library.dto.mapper.UserMapper;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DisplayReadersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private static final int RECORDS_PER_PAGE = 5;

    private final UserService userService;

    public DisplayReadersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "DisplayReadersCommand invoked");
        HttpSession session = request.getSession();

        session.setAttribute(Parameters.PREVIOUS_PAGE, RedirectToPage.DISPLAY_READERS);

        int currPage = 1;

        var reqCurrPage = request.getParameter(Parameters.GENERAL_CURR_PAGE);
        logger.log(Level.DEBUG, "DisplayReadersCommand/ current page: " + reqCurrPage);

        if (reqCurrPage != null && !reqCurrPage.isBlank()) {
            currPage = Integer.parseInt(reqCurrPage);
        }
        try {
            List<User> usersList = userService.findAllReaders(
                    (currPage - 1) * RECORDS_PER_PAGE,
                    RECORDS_PER_PAGE);

            int totalRecords = userService.countReaders(true);

            logger.log(Level.DEBUG, "DisplayReadersCommand/ total orders: " + totalRecords);

            var totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            List<UserDTO> usersDTOList = new UserMapper().toDTOList(usersList);

            request.setAttribute(Parameters.GENERAL_CURR_PAGE, currPage);
            request.setAttribute(Parameters.GENERAL_TOTAL_PAGES, totalPages);
            request.setAttribute(Parameters.USERS_LIST, usersDTOList);
            request.setAttribute(Parameters.USERS_PER_PAGE, RECORDS_PER_PAGE);

            return new CommandResult(Pages.DISPLAY_USERS_PAGE, CommandDirection.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("Error while executing DisplayReadersCommand", e);
        }
    }
}
