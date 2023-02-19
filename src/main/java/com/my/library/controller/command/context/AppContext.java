package com.my.library.controller.command.context;

import com.my.library.connection_pool.ConnectionPool;
import com.my.library.controller.command.CommandFactory;
import com.my.library.dao.DaoFactory;
import com.my.library.services.ServiceFactory;

public class AppContext {

    private AppContext() {
    }

    private static final class InstanceHolder {
        private static final AppContext instance = new AppContext();
    }

    public static AppContext getInstance() {
        return InstanceHolder.instance;
    }


//    public ServiceFactory getServiceFactory() {
//        var connection = ConnectionPool.getInstance().getConnection();
//        return new ServiceFactory( new DaoFactory(connection));
//    }

    public CommandFactory getCommandFactory() {
        var connection = ConnectionPool.getInstance().getConnection();
        return new CommandFactory(connection, new ServiceFactory(new DaoFactory(connection)));
    }
}
