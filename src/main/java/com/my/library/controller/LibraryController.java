package com.my.library.controller;

import com.my.library.controller.command.CommandFactory;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.commands.GeneralCommands;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandReq = request.getParameter(GeneralCommands.COMMAND_PARAMETER);

        try (var commandFactory = new CommandFactory()){
            var command = commandFactory.createCommand(commandReq);

            logger.log(Level.DEBUG, "Command " + command.getClass().getSimpleName() + " was received");

            var commandRes = command.execute(request);
            direct(request, response, commandRes);

        } catch (CommandException e) {
            logger.log(Level.ERROR, "Command exception while processingRequest", e);
            response.sendRedirect(request.getContextPath() +  RedirectToPage.ERROR_PAGE);
        }
    }

    private void direct(HttpServletRequest request, HttpServletResponse response, CommandResult commandResult) throws ServletException, IOException {
        switch (commandResult.getAction()) {
            case FORWARD ->
                    getServletContext().getRequestDispatcher(commandResult.getPage()).forward(request, response);
            case REDIRECT -> response.sendRedirect(request.getContextPath() + commandResult.getPage());
            default -> response.sendRedirect(request.getContextPath() + RedirectToPage.HOME);
        }
    }
}
