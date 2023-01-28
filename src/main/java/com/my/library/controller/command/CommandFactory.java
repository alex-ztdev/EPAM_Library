package com.my.library.controller.command;

import com.my.library.controller.command.constant.AdminCommands;
import com.my.library.controller.command.constant.GeneralCommands;
import com.my.library.controller.command.constant.UserCommands;
import com.my.library.controller.command.impl.admin.RemoveBookCommand;
import com.my.library.controller.command.impl.common.*;
import com.my.library.services.ServiceFactory;

public class CommandFactory {

    public static Command createCommand(String command) {
        Command res;
        switch (command) {
            case GeneralCommands.LOGIN -> res = new LoginCommand();
            case GeneralCommands.LOGOUT -> res = new LogoutCommand();
            case GeneralCommands.REGISTRATION -> res = new RegisterCommand();
            case GeneralCommands.CHANGE_LANGUAGE -> res = new ChangeLanguageCommand();
            case GeneralCommands.BOOKS_LIST -> res = new DisplayBooksListCommand();
            case AdminCommands.REMOVE_BOOK -> res = new RemoveBookCommand();
            default -> res = new DefaultCommand();
        }
        return res;
    }

}
