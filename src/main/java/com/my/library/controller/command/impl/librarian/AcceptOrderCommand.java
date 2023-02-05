package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AcceptOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        logger.log(Level.DEBUG, "AcceptOrderCommand invoked");

        return null;
    }
}
