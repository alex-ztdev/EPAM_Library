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
            	Books.author_id
            FROM Books
            INNER JOIN Publishers ON Books.publisher_id = Publishers.id
            INNER JOIN Book_Genres ON Books.genre_id = Book_Genres.id
            """;
    String FIND_BOOK_BY_ID = FIND_ALL_BOOKS + "WHERE Books.id=?";

    //language=TSQL
    String INSERT_BOOK = """
            INSERT INTO Books
            (title, publisher_id, genre_id, page_number, publication_date, author_id )
            VALUES
            (?, ( SELECT TOP(1) id FROM Publishers WHERE title=?),
             ( SELECT TOP(1) id FROM Book_Genres WHERE title=?), ?,?,?)
            """;
    //language=TSQL
    String UPDATE_BOOK = """
            UPDATE Books SET
            title= ?,
            publisher_id= (SELECT TOP(1) id FROM Publishers WHERE title=?),
            genre_id=(SELECT TOP(1) id FROM Book_Genres WHERE title=?),
            page_number=?,
            publication_date=?
            WHERE id = ?
            """;

    //language=TSQL
    String SET_BOOK_TO_REMOVED = """
            UPDATE Storage SET
            isRemoved=?
            WHERE book_id = ?
            """;

    //language=TSQL
    String FIND_ALL_BOOKS_AUTHOR = """
            SELECT author_id FROM Books
            WHERE id =?
            """;

    //language=TSQL
    String FIND_ALL_NOT_REMOVED_BOOKS_PAGINATION = FIND_ALL_BOOKS + """
            INNER JOIN Storage S on Books.id = S.book_id
            WHERE isRemoved = 0
            ORDER BY %s %s
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;
  //language=TSQL
    String FIND_ALL_BOOKS_PAGINATION = FIND_ALL_BOOKS + """
            INNER JOIN Storage S on Books.id = S.book_id
            ORDER BY %s %s
            OFFSET ? ROWS
            FETCH NEXT ? ROWS ONLY
            """;


    //language=TSQL
    String COUNT_ALL_BOOK_RECORDS = """
    SELECT COUNT(Books.id) FROM Books
    INNER JOIN Storage S on Books.id = S.book_id
    """;

    //language=TSQL
    String COUNT_NOT_REMOVED_BOOK_RECORDS = COUNT_ALL_BOOK_RECORDS + "WHERE isRemoved=0";

    //language=TSQL
    String IS_REMOVED_BOOK = """
            SELECT isRemoved FROM Storage WHERE book_id=?
            """;
    //language=TSQL
    String GET_QUANTITY = """
            SELECT quantity FROM Storage WHERE book_id=?
            """;

}
