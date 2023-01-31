package com.my.library.controller.command;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.controller.command.constant.commands.AdminCommands;
import com.my.library.controller.command.constant.commands.GeneralCommands;
import com.my.library.controller.command.constant.commands.UserCommands;
import com.my.library.controller.command.impl.admin.*;
import com.my.library.controller.command.impl.common.*;
import com.my.library.controller.command.impl.user.OrderBookRedirect;
import com.my.library.dao.TransactionManager;
import com.my.library.services.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandFactory implements AutoCloseable {
    private final static Logger logger = LogManager.getLogger();

    private ServiceFactory serviceFactory;
    private Connection connection;

    public CommandFactory() {
        this.connection = ConnectionPool.getInstance().getConnection();
        this.serviceFactory = new ServiceFactory(connection);
    }

    //TODO: implement initialization only when command need services!
    public Command createCommand(String command) {
        Command res;
        switch (command) {
            case GeneralCommands.LOGOUT -> res = new LogoutCommand();
            case GeneralCommands.HOME -> res = new HomeCommand();
            case GeneralCommands.CHANGE_LANGUAGE -> res = new ChangeLanguageCommand();

            case GeneralCommands.LOGIN -> res = new LoginCommand(serviceFactory.getUserService());
            case GeneralCommands.REGISTRATION -> res = new RegisterCommand(serviceFactory.getUserService());

            case GeneralCommands.BOOKS_LIST -> res = new DisplayBooksListCommand(serviceFactory.getBookService());
            case AdminCommands.REMOVE_BOOK -> res = new RemoveBookCommand(serviceFactory.getBookService());
            case AdminCommands.RESTORE_BOOK -> res = new RestoreBookCommand(serviceFactory.getBookService());
            case AdminCommands.ADD_BOOK_REDIRECT -> res = new AddBookRedirectCommand(serviceFactory.getGenreService(), serviceFactory.getPublisherService());
            case AdminCommands.UPDATE_BOOK_REDIRECT -> res = new UpdateBookRedirectCommand(serviceFactory.getBookService(), serviceFactory.getGenreService(), serviceFactory.getPublisherService());
            case AdminCommands.UPDATE_BOOK -> res = new UpdateBookCommand(serviceFactory.getBookService(), serviceFactory.getAuthorService(), new TransactionManager(connection));
            case AdminCommands.ADD_BOOK ->
                    res = new AddBookCommand(serviceFactory.getBookService(), serviceFactory.getAuthorService(), new TransactionManager(connection));

            case UserCommands.ORDER_BOOK_REDIRECT -> res = new OrderBookRedirect(serviceFactory.getBookService());
            default -> res = new DefaultCommand();
        }
        return res;
    }

    @Override
    public void close() {
        //TODO: implement retrieve connection
        try {
            connection.close();
//            logger.log(Level.DEBUG, "Connection successfully retrieved after CommandFactoryClosed");
        } catch (SQLException e) {
            logger.error("Error while closing connection", e);
        }
    }
}
