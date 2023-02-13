package com.my.library.controller.command;

import com.my.library.controller.command.constant.commands.AdminCommands;
import com.my.library.controller.command.constant.commands.GeneralCommands;
import com.my.library.controller.command.constant.commands.LibrarianCommands;
import com.my.library.controller.command.constant.commands.UserCommands;
import com.my.library.controller.command.impl.admin.*;
import com.my.library.controller.command.impl.common.*;
import com.my.library.controller.command.impl.librarian.*;
import com.my.library.controller.command.impl.user.*;
import com.my.library.services.ServiceFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    @Mock
    Connection connection;

    @Mock
    ServiceFactory serviceFactory;

    @InjectMocks
    CommandFactory commandFactory;

    @Test
    void createCommand_GeneralCommands() {
        Command logoutCommand = commandFactory.createCommand(GeneralCommands.LOGOUT);
        assertThat(logoutCommand).isInstanceOf(LogoutCommand.class);

        Command homeCommand = commandFactory.createCommand(GeneralCommands.HOME);
        assertThat(homeCommand).isInstanceOf(HomeCommand.class);

        Command changeLanguageCommand = commandFactory.createCommand(GeneralCommands.CHANGE_LANGUAGE);
        assertThat(changeLanguageCommand).isInstanceOf(ChangeLanguageCommand.class);

        Command notAuthorizedCommand = commandFactory.createCommand(GeneralCommands.NOT_AUTHORIZED);
        assertThat(notAuthorizedCommand).isInstanceOf(NotAuthorizedCommand.class);

        Command loginCommand = commandFactory.createCommand(GeneralCommands.LOGIN);
        assertThat(loginCommand).isInstanceOf(LoginCommand.class);

        Command loginRedirectCommand = commandFactory.createCommand(GeneralCommands.LOGIN_PAGE);
        assertThat(loginRedirectCommand).isInstanceOf(LoginRedirectCommand.class);

        Command registerCommand = commandFactory.createCommand(GeneralCommands.REGISTRATION);
        assertThat(registerCommand).isInstanceOf(RegisterCommand.class);

        Command searchBookCommand = commandFactory.createCommand(GeneralCommands.SEARCH_BOOK);
        assertThat(searchBookCommand).isInstanceOf(SearchBookCommand.class);

        Command unsupportedOperationCommand = commandFactory.createCommand(GeneralCommands.UNSUPPORTED_OPERATION);
        assertThat(unsupportedOperationCommand).isInstanceOf(UnsupportedOperationCommand.class);

        Command errorPageCommand = commandFactory.createCommand(GeneralCommands.ERROR_PAGE);
        assertThat(errorPageCommand).isInstanceOf(ErrorPageCommand.class);

        Command bookListCommand = commandFactory.createCommand(GeneralCommands.BOOKS_LIST);
        assertThat(bookListCommand).isInstanceOf(DisplayBooksListCommand.class);

        Command defaultCommand = commandFactory.createCommand(GeneralCommands.DEFAULT_COMMAND);
        assertThat(defaultCommand).isInstanceOf(DefaultCommand.class);
    }

    @Test
    void createCommand_AdminCommands() {
        Command removeBookCommand = commandFactory.createCommand(AdminCommands.REMOVE_BOOK);
        assertTrue(removeBookCommand instanceof RemoveBookCommand);

        Command restoreBookCommand = commandFactory.createCommand(AdminCommands.RESTORE_BOOK);
        assertTrue(restoreBookCommand instanceof RestoreBookCommand);

        Command addBookRedirectCommand = commandFactory.createCommand(AdminCommands.ADD_BOOK_REDIRECT);
        assertTrue(addBookRedirectCommand instanceof AddBookRedirectCommand);

        Command updateBookRedirectCommand = commandFactory.createCommand(AdminCommands.UPDATE_BOOK_REDIRECT);
        assertTrue(updateBookRedirectCommand instanceof UpdateBookRedirectCommand);

        Command updateBookCommand = commandFactory.createCommand(AdminCommands.UPDATE_BOOK);
        assertTrue(updateBookCommand instanceof UpdateBookCommand);

        Command addBookCommand = commandFactory.createCommand(AdminCommands.ADD_BOOK);
        assertTrue(addBookCommand instanceof AddBookCommand);

        Command displayUsersCommand = commandFactory.createCommand(AdminCommands.DISPLAY_USERS);
        assertTrue(displayUsersCommand instanceof DisplayUsersCommand);

        Command blockUserCommand = commandFactory.createCommand(AdminCommands.BLOCK_USER);
        assertTrue(blockUserCommand instanceof BlockUserCommand);

        Command unblockUserCommand = commandFactory.createCommand(AdminCommands.UNBLOCK_USER);
        assertTrue(unblockUserCommand instanceof UnblockUserCommand);

        Command changeRoleCommand = commandFactory.createCommand(AdminCommands.CHANGE_ROLE);
        assertTrue(changeRoleCommand instanceof ChangeRoleCommand);
    }

    @Test
    void testCreateCommand_UserCommands() {


        Command orderBookRedirect = commandFactory.createCommand(UserCommands.ORDER_BOOK_REDIRECT);
        assertThat(orderBookRedirect).isInstanceOf(OrderBookRedirectCommand.class);

        Command myProfile = commandFactory.createCommand(UserCommands.MY_PROFILE);
        assertThat(myProfile).isInstanceOf(MyProfileCommand.class);


        Command orderBook = commandFactory.createCommand(UserCommands.ORDER_BOOK);
        assertThat(orderBook).isInstanceOf(OrderBookCommand.class);

        Command displayMyOrders = commandFactory.createCommand(UserCommands.DISPLAY_MY_ORDERS);
        assertThat(displayMyOrders).isInstanceOf(DisplayMyOrdersCommand.class);

        Command displayMyRequests = commandFactory.createCommand(UserCommands.DISPLAY_MY_REQUESTS);
        assertThat(displayMyRequests).isInstanceOf(DisplayMyRequestsCommand.class);

        Command cancelOrder = commandFactory.createCommand(UserCommands.CANCEL_ORDER);
        assertThat(cancelOrder).isInstanceOf(CancelOrderCommand.class);

    }

    @Test
    void testCreateCommand_LibrarianCommands() {
        Command displayUsersOrders = commandFactory.createCommand(LibrarianCommands.DISPLAY_USERS_ORDERS);
        assertThat(displayUsersOrders).isInstanceOf(DisplayUsersOrdersCommand.class);

        Command returnOrder = commandFactory.createCommand(LibrarianCommands.RETURN_ORDER);
        assertThat(returnOrder).isInstanceOf(ReturnOrderCommand.class);

        Command displayReaders = commandFactory.createCommand(LibrarianCommands.DISPLAY_READERS);
        assertThat(displayReaders).isInstanceOf(DisplayReadersCommand.class);

        Command requestedOrders = commandFactory.createCommand(LibrarianCommands.DISPLAY_REQUESTED_ORDERS);
        assertThat(requestedOrders).isInstanceOf(DisplayUsersRequestedOrdersCommand.class);

        Command acceptOrder = commandFactory.createCommand(LibrarianCommands.ACCEPT_ORDER);
        assertThat(acceptOrder).isInstanceOf(AcceptOrderCommand.class);

        Command declineOrder = commandFactory.createCommand(LibrarianCommands.DECLINE_ORDER);
        assertThat(declineOrder).isInstanceOf(DeclineOrderCommand.class);
    }


    @Test
    void closeMethod_Success() throws SQLException {
        commandFactory.close();

        verify(connection,times(1)).close();
    }

}