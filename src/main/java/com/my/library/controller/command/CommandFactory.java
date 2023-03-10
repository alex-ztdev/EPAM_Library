package com.my.library.controller.command;

import com.my.library.controller.command.constant.commands.AdminCommands;
import com.my.library.controller.command.constant.commands.GeneralCommands;
import com.my.library.controller.command.constant.commands.LibrarianCommands;
import com.my.library.controller.command.constant.commands.UserCommands;
import com.my.library.controller.command.impl.admin.*;
import com.my.library.controller.command.impl.common.*;
import com.my.library.controller.command.impl.librarian.*;
import com.my.library.controller.command.impl.user.*;
import com.my.library.dao.TransactionManager;
import com.my.library.services.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandFactory implements AutoCloseable {
    private final static Logger logger = LogManager.getLogger();
    private final ServiceFactory serviceFactory;
    private final Connection connection;

    public CommandFactory(Connection connection, ServiceFactory serviceFactory) {
        this.connection = connection;
        this.serviceFactory = serviceFactory;
    }

    //TODO: implement initialization only when command need services!
    public Command createCommand(String command) {
        Command res;
        switch (command) {
            case GeneralCommands.LOGOUT -> res = new LogoutCommand();
            case GeneralCommands.HOME -> res = new HomeCommand();
            case GeneralCommands.CHANGE_LANGUAGE -> res = new ChangeLanguageCommand();
            case GeneralCommands.NOT_AUTHORIZED -> res = new NotAuthorizedCommand();

            case GeneralCommands.LOGIN -> res = new LoginCommand(serviceFactory.getUserService());
            case GeneralCommands.LOGIN_PAGE -> res = new LoginRedirectCommand();
            case GeneralCommands.REGISTRATION -> res = new RegisterCommand(serviceFactory.getUserService());
            case GeneralCommands.SEARCH_BOOK -> res = new SearchBookCommand(serviceFactory.getBookService());
            case GeneralCommands.UNSUPPORTED_OPERATION -> res = new UnsupportedOperationCommand();
            case GeneralCommands.ERROR_PAGE -> res = new ErrorPageCommand();

            case GeneralCommands.BOOKS_LIST -> res = new DisplayBooksListCommand(serviceFactory.getBookService());
            case AdminCommands.REMOVE_BOOK -> res = new RemoveBookCommand(serviceFactory.getBookService());
            case AdminCommands.RESTORE_BOOK -> res = new RestoreBookCommand(serviceFactory.getBookService());
            case AdminCommands.ADD_BOOK_REDIRECT -> res = new AddBookRedirectCommand(serviceFactory.getGenreService(), serviceFactory.getPublisherService());
            case AdminCommands.UPDATE_BOOK_REDIRECT -> res = new UpdateBookRedirectCommand(serviceFactory.getBookService(), serviceFactory.getGenreService(), serviceFactory.getPublisherService());
            case AdminCommands.UPDATE_BOOK -> res = new UpdateBookCommand(serviceFactory.getBookService(), serviceFactory.getAuthorService(), new TransactionManager(connection));
            case AdminCommands.ADD_BOOK ->
                    res = new AddBookCommand(serviceFactory.getBookService(), serviceFactory.getAuthorService(), new TransactionManager(connection));
            case AdminCommands.DISPLAY_USERS-> res = new DisplayUsersCommand(serviceFactory.getUserService());
            case AdminCommands.BLOCK_USER-> res = new BlockUserCommand(serviceFactory.getUserService());
            case AdminCommands.UNBLOCK_USER-> res = new UnblockUserCommand(serviceFactory.getUserService());
            case AdminCommands.CHANGE_ROLE-> res = new ChangeRoleCommand(serviceFactory.getUserService());

            case UserCommands.ORDER_BOOK_REDIRECT -> res = new OrderBookRedirectCommand(serviceFactory.getBookService());
            case UserCommands.MY_PROFILE -> res = new MyProfileCommand();

            case UserCommands.ORDER_BOOK ->
                    res = new OrderBookCommand(serviceFactory.getOrderService(), serviceFactory.getBookService(), new TransactionManager(connection));
            case UserCommands.DISPLAY_MY_ORDERS ->
                    res = new DisplayMyOrdersCommand(serviceFactory.getBookService(), serviceFactory.getUserService(), serviceFactory.getOrderService());
            case UserCommands.DISPLAY_MY_REQUESTS ->
                    res = new DisplayMyRequestsCommand(serviceFactory.getOrderService(), serviceFactory.getUserService(), serviceFactory.getBookService());
            case UserCommands.CANCEL_ORDER -> res = new CancelOrderCommand(serviceFactory.getOrderService(), serviceFactory.getBookService(), new TransactionManager(connection));

            case LibrarianCommands.DISPLAY_USERS_ORDERS ->
                    res = new DisplayUsersOrdersCommand(serviceFactory.getBookService(), serviceFactory.getUserService(), serviceFactory.getOrderService());
            case LibrarianCommands.RETURN_ORDER ->
                    res = new ReturnOrderCommand(serviceFactory.getBookService(), serviceFactory.getOrderService(), new TransactionManager(connection));
            case LibrarianCommands.DISPLAY_READERS -> res = new DisplayReadersCommand(serviceFactory.getUserService());
            case LibrarianCommands.DISPLAY_REQUESTED_ORDERS -> res = new DisplayUsersRequestedOrdersCommand(serviceFactory.getOrderService(), serviceFactory.getBookService(), serviceFactory.getUserService());
            case LibrarianCommands.ACCEPT_ORDER -> res = new AcceptOrderCommand(serviceFactory.getOrderService());
            case LibrarianCommands.DECLINE_ORDER ->
                    res = new DeclineOrderCommand(serviceFactory.getOrderService(), serviceFactory.getBookService(), new TransactionManager(connection));
            default -> res = new DefaultCommand();
        }
        return res;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error while closing connection", e);
        }
    }
}
