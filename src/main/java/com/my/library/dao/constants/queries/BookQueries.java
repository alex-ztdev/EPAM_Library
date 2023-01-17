package com.my.library.dao.constants.queries;

public interface BookQueries {
    String FIND_ALL_BOOKS= """
            SELECT
            	Books.id,
            	Books.title,
            	Publishers.title AS publisher,
            	Book_Genres.title as book_genre,
            	Books.page_number,
            	Books.publication_date,
            	Books.isAvailable
            FROM Books
            INNER JOIN Publishers ON Books.publisher_id = Publishers.id
            INNER JOIN Book_Genres ON Books.genre_id = Book_Genres.id
            """;
    String FIND_BOOK_BY_ID = FIND_ALL_BOOKS + "WHERE Books.id=?";


    String INSERT_BOOK = """
            INSERT INTO Books
            (title, publisher_id, genre_id, page_number, publication_date, isAvailable )
            VALUES
            (?, ( SELECT TOP(1) id FROM Publishers WHERE title=?),
             ( SELECT TOP(1) id FROM Book_Genres WHERE title=?), ?,?,?)
            """;

    String UPDATE_BOOK = """
            UPDATE Books SET
            title= ?,
            publisher_id= (SELECT TOP(1) id FROM Publishers WHERE title=?),
            genre_id=(SELECT TOP(1) id FROM Book_Genres WHERE title=?),
            page_number=?,
            publication_date=?,
            isAvailable=?
            WHERE id = ?
            """;

    String SET_BOOK_TO_REMOVED = """
            UPDATE Books SET
            isAvailable=?
            WHERE id = ?
            """;
}
