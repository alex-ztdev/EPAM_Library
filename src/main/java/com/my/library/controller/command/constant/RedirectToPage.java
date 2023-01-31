package com.my.library.controller.command.constant;

public interface RedirectToPage {
    String LOGIN_PAGE = "/controller?command=login";
    String BOOKS_PAGE = "/controller?command=books-list";

    String BOOKS_PAGE_WITH_PARAMETERS = "/controller?command=books-list&order_by=%s&order_dir=%s&page=%s";
    String BOOKS_UPDATE_PAGE_WITH_PARAMETER = "/controller?command=update-book-redirect&book_id=%s";
    String BOOKS_ADD_PAGE = "/controller?command=add-book-redirect";
    String MY_ORDERS_PAGE = "/controller?command=display-orders";
}
