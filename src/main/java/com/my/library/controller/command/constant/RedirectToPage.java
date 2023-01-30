package com.my.library.controller.command.constant;

public interface RedirectToPage {
    String LOGIN_PAGE = "/controller?command=login";
    String BOOKS_PAGE = "/controller?command=books-list";

    String BOOKS_PAGE_WITH_PARAMETERS = "/controller?command=books-list&order_by=%s&order_dir=%s&page=%s";
    String BOOKS_EDIT_PAGE_WITH_PARAMETER = "/controller?command=update-book-redirect&book_id=%s";

}
