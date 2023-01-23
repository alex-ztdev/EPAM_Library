package com.my.library.controller.command;

import com.my.library.controller.command.constant.CommandTypes;
import com.my.library.controller.command.impl.ChangeLanguageCommand;
import com.my.library.controller.command.impl.LoginCommand;
import com.my.library.controller.command.impl.RegisterCommand;
import com.my.library.services.ServiceFactory;

public class CommandFactory {
    private final ServiceFactory serviceFactory;

    public CommandFactory() {
        this.serviceFactory = new ServiceFactory();
    }

    public static Command createCommand(String command) {
        Command res;
        switch (command) {
            case CommandTypes.LOGIN -> res = new LoginCommand();
            case CommandTypes.REGISTRATION -> res = new RegisterCommand();
            case CommandTypes.CHANGE_LANGUAGE -> res = new ChangeLanguageCommand();
            default -> res = null;
        }
        return res;
    }

}
