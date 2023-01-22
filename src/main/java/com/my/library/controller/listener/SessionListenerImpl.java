package com.my.library.controller.listener;

import com.my.library.dao.constants.columns.UsersColumns;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionListenerImpl implements HttpSessionListener, HttpSessionAttributeListener {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.log(Level.INFO, "Session created: " + se.getSession().getId());
//        + se.getSession().getAttribute(UsersColumns.LOGIN)
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.log(Level.INFO, "Session destroyed: " + se.getSession().getId());
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        logger.log(Level.INFO, "Session id:" +sbe.getSession().getId() +" attributeAdded: " + sbe.getSession().getAttribute(UsersColumns.LOGIN));
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        logger.log(Level.INFO, "Session id:" +sbe.getSession().getId() +" attributeReplaced: " + sbe.getSession().getAttribute(UsersColumns.LOGIN));
    }
}
