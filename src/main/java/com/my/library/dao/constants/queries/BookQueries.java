package com.my.library.dao.constants.queries;

public interface BookQueries {
    String GET_BOOK_BY_ID = """
            SELECT
            	Books.id,
            	Books.title,
            	Publishers.title AS publisher,
            	Book_Genres.title as book_title,
            	Books.page_number,
            	Books.publication_date,
            	Books.isAvailable
            FROM Books
            INNER JOIN Publishers ON Books.publisher_id = Publishers.id
            INNER JOIN Book_Genres ON Books.genre_id = Book_Genres.id
            WHERE Books.id=?
            """;


}
