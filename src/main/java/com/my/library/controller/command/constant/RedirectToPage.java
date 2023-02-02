package com.my.library.controller.command.constant;

public interface RedirectToPage {
    String BOOKS_PAGE = "/controller?command=books-list";

    String BOOKS_PAGE_WITH_PARAMETERS = "/controller?command=books-list&order_by=%s&order_dir=%s&page=%s";
    String BOOKS_UPDATE_PAGE_WITH_PARAMETER = "/controller?command=update-book-redirect&book_id=%s";
    String BOOKS_ADD_PAGE = "/controller?command=add-book-redirect";
    String MY_ORDERS_PAGE_WITH_SUCCESSFUL_MSG = "/controller?command=display-my-orders&msg=success";
    String MY_ORDERS_PAGE = "/controller?command=display-my-orders";
    String DISPLAY_USERS_ORDERS = "/controller?command=display-users-orders";
    String NOT_AUTHORIZED = "/controller?command=not-authorized";
    String HOME = "/controller?command=home";

    String LOGIN_PAGE = "/controller?command=login-page";
    String LOGIN_PAGE_WITH_SUCCESS = "/controller?command=login-page&reg_msg=successful_registration&reg_invoked=true";
    String REGISTRATION_PAGE = "/controller?command=login-page&regForm=regForm";

    String DISPLAY_USERS = "/controller?command=display-users";
    String DISPLAY_READERS = "/controller?command=display-readers";
    String MY_PROFILE = "/controller?command=my-profile";

}
