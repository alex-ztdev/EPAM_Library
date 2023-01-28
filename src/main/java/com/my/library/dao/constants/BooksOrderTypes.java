package com.my.library.dao.constants;

public enum BooksOrderTypes {
    BY_ID("Books.id"),
    BY_TITLE("Books.title"),

    BY_PUBLISHER("publisher"),
    BY_AUTHOR("A.first_name +' '+ A.second_name"),
    BY_GENRE("book_genre"),
    BY_PUBLICATION_DATE("publication_date"),
    BY_PAGES("Books.page_number"),
    BY_COPIES("S.quantity"),
    BY_REMOVED("S.isRemoved");

    private final String orderBy;

    BooksOrderTypes(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
