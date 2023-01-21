package com.my.library.controller;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.UserService;
import com.my.library.services.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;

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

//        try (CommandFactory factory = new CommandFactory()) {
//            Command action = factory.create(command);
//            CommandResult commandResult = action.execute(request, response);
//            dispatch(request, response, commandResult);
//        } catch (ServiceException e) {
//            logger.error("Exception in Library Controller", e);
//            response.sendRedirect(PageLocation.ERROR_PAGE);
//        }
        UserService userService = UserServiceImpl.getInstance();

        try {
            userService.authenticate(request.getParameter("login"), request.getParameter("password"));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        super.destroy();
    }
}
