package com.my.library.controller.listener;

import com.my.library.controller.command.constant.UserParameters;
import com.my.library.entities.User;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionListenerImpl implements HttpSessionListener, HttpSessionAttributeListener {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.log(Level.DEBUG, "Session created: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.log(Level.DEBUG, "Session destroyed: " + se.getSession().getId());
    }
// TODO: remove or add listener methods

//    @Override
//    public void attributeAdded(HttpSessionBindingEvent sbe) {
//        User user = (User) sbe.getSession().getAttribute(UserParameters.USER_IN_SESSION);
//        logger.log(Level.INFO, "Session id:" +sbe.getSession().getId() +" attribute user added: " + "user_id="
//                +(user == null ? null : user.getUserId()));
//    }
//
//    @Override
//    public void attributeRemoved(HttpSessionBindingEvent sbe) {
//        /* This method is called when an attribute is removed from a session. */
//    }
//
//    @Override
//    public void attributeReplaced(HttpSessionBindingEvent sbe) {
//        User user = (User) sbe.getSession().getAttribute(UserParameters.USER_IN_SESSION);
//        logger.log(Level.INFO, "Session id:" +sbe.getSession().getId() +" attribute replaced: "+ "user_id:"
//                +(user == null ? null : user.getUserId()));
//    }
}
