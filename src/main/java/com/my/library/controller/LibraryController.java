package com.my.library.controller;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.controller.command.CommandFactory;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.exceptions.CommandException;
import com.my.library.utils.Pages;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/controller")
public class LibraryController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private static final String COMMAND_NAME = "command";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter(COMMAND_NAME);
        var action = CommandFactory.createCommand(command);
        try {
            var commandRes = action.execute(request);
            direct(request, response, commandRes);
        } catch (CommandException e) {
            logger.log(Level.ERROR, "Command exception while processingRequest", e);
            response.sendRedirect(Pages.ERROR_PAGE);
        }
    }

    private void direct(HttpServletRequest request, HttpServletResponse response, CommandResult commandResult) throws ServletException, IOException {
        switch (commandResult.getAction()) {
            case FORWARD -> request.getRequestDispatcher(commandResult.getPage()).forward(request, response);
            case REDIRECT -> response.sendRedirect(commandResult.getPage());
            default -> response.sendRedirect(RedirectToPage.LOGIN_PAGE);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        super.destroy();
    }
}
