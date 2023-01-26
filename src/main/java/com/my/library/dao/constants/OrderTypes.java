package com.my.library.dao.constants;

public enum OrderTypes {
    BY_ID("Books.id"),
    BY_TITLE("title"),

    BY_PUBLISHER("publisher"),
    BY_AUTHOR("author"),
    BY_GENRE("book_genre"),
    BY_PUBLICATION_DATE("publication_date");

    private final String orderBy;

    OrderTypes(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
