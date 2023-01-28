package com.my.library.controller.command.constant;

public interface RedirectToPage {
    String LOGIN_PAGE = "/controller?command=login";
    String BOOKS_PAGE = "/controller?command=books-list";

    String BOOKS_PAGE_WITH_PARAMETERS = "/controller?command=books-list&order_by=%s&order_dir=%s&page=%s";
}
