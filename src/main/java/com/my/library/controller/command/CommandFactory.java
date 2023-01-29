package com.my.library.controller.command;

import com.my.library.controller.command.constant.commands.AdminCommands;
import com.my.library.controller.command.constant.commands.GeneralCommands;
import com.my.library.controller.command.impl.admin.AddBookRedirectCommand;
import com.my.library.controller.command.impl.admin.RemoveBookCommand;
import com.my.library.controller.command.impl.admin.RestoreBookCommand;
import com.my.library.controller.command.impl.admin.UpdateBookRedirectCommand;
import com.my.library.controller.command.impl.common.*;

public class CommandFactory {

    public static Command createCommand(String command) {
        Command res;
        switch (command) {
            case GeneralCommands.LOGIN -> res = new LoginCommand();
            case GeneralCommands.LOGOUT -> res = new LogoutCommand();
            case GeneralCommands.REGISTRATION -> res = new RegisterCommand();
            case GeneralCommands.CHANGE_LANGUAGE -> res = new ChangeLanguageCommand();
            case GeneralCommands.HOME -> res = new HomeCommand();
            case GeneralCommands.BOOKS_LIST -> res = new DisplayBooksListCommand();
            case AdminCommands.REMOVE_BOOK -> res = new RemoveBookCommand();
            case AdminCommands.RESTORE_BOOK -> res = new RestoreBookCommand();
            case AdminCommands.ADD_BOOK_REDIRECT -> res = new AddBookRedirectCommand();
            case AdminCommands.UPDATE_BOOK_REDIRECT -> res = new UpdateBookRedirectCommand();
            default -> res = new DefaultCommand();
        }
        return res;
    }

}
