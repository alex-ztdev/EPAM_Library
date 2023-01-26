package com.my.library.dao.constants.queries;

public interface BookQueries {
    //language=TSQL
    String FIND_ALL_BOOKS = """
            SELECT
            	Books.id,
            	Books.title,
            	Publishers.title AS publisher,
            	Book_Genres.title as book_genre,
            	Books.page_number,
            	Books.publication_date,
            	Books.isAvailable,
            	Books.isRemoved
            FROM Books
            INNER JOIN Publishers ON Books.publisher_id = Publishers.id
            INNER JOIN Book_Genres ON Books.genre_id = Book_Genres.id
            """;
    String FIND_BOOK_BY_ID = FIND_ALL_BOOKS + "WHERE Books.id=?";

    //language=TSQL
    String INSERT_BOOK = """
            INSERT INTO Books
            (title, publisher_id, genre_id, page_number, publication_date, isAvailable,isRemoved )
            VALUES
            (?, ( SELECT TOP(1) id FROM Publishers WHERE title=?),
             ( SELECT TOP(1) id FROM Book_Genres WHERE title=?), ?,?,?,?)
            """;
    //language=TSQL
    String UPDATE_BOOK = """
            UPDATE Books SET
            title= ?,
            publisher_id= (SELECT TOP(1) id FROM Publishers WHERE title=?),
            genre_id=(SELECT TOP(1) id FROM Book_Genres WHERE title=?),
            page_number=?,
            publication_date=?,
            isAvailable=?,
            isRemoved=?
            WHERE id = ?
            """;

    //language=TSQL
    String SET_BOOK_TO_REMOVED = """
            UPDATE Books SET
            isRemoved=?
            WHERE id = ?
            """;

    //language=TSQL
    String FIND_ALL_BOOKS_AUTHORS = """
            SELECT author_id FROM Books
            WHERE id =?
            """;

    //language=TSQL
    String FIND_ALL_BOOKS_PAGINATION = FIND_ALL_BOOKS + """
            WHERE isAvailable = 1
            ORDER BY ?
            OFFSET (?-1) ROWS
            FETCH NEXT ? ROWS ONLY
            """;
}
