package com.my.library.utils.validator;

import com.my.library.entities.Book;

import java.util.Objects;
import java.util.regex.Pattern;

public class BookValidator {
    private static final String TITLE_PATTERN = "^['a-zA-Z?!,.а-яА-ЯёЁ\\d\\s-]{1,350}$";
    private static final String NAME_PATTERN = "^['a-zA-Z?а-яА-ЯёЁ]{1,50}$";

    public boolean validateBook(Book book) {
        if (book == null || book.getAuthor() == null
                || book.getPublicationDate() == null
                || book.getGenre() == null
                || book.getTitle() == null
                || book.getPublisherTitle() == null) {
            return false;
        } else return Pattern.matches(TITLE_PATTERN, book.getTitle())
                && Pattern.matches(NAME_PATTERN, book.getAuthor().getFirstName())
                && Pattern.matches(NAME_PATTERN, book.getAuthor().getSecondName());
    }

}
