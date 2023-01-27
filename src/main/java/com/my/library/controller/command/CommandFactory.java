package com.my.library.controller.command;

import com.my.library.controller.command.constant.GeneralCommands;
import com.my.library.controller.command.constant.UserCommands;
import com.my.library.controller.command.impl.common.*;
import com.my.library.services.ServiceFactory;

public class CommandFactory {
    private final ServiceFactory serviceFactory;

    public CommandFactory() {
        this.serviceFactory = new ServiceFactory();
    }

    public static Command createCommand(String command) {
        Command res;
        switch (command) {
            case GeneralCommands.LOGIN -> res = new LoginCommand();
            case GeneralCommands.LOGOUT -> res = new LogoutCommand();
            case GeneralCommands.REGISTRATION -> res = new RegisterCommand();
            case GeneralCommands.CHANGE_LANGUAGE -> res = new ChangeLanguageCommand();
            case UserCommands.BOOKS_LIST -> res = new DisplayBooksListCommand();
            default -> res = new DefaultCommand();
        }
        return res;
    }

}
