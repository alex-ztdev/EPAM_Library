package com.my.library.dao.constants.queries;

public interface AuthorQueries {
    //language=TSQL
    String FIND_AUTHOR_BY_ID = "SELECT id, first_name, second_name, birth_date FROM Authors WHERE id = ?";
    //language=TSQL
    String FIND_ALL_AUTHORS = "SELECT id, first_name, second_name, birth_date FROM Authors";
    //language=TSQL
    String INSERT_AUTHOR = "INSERT INTO Authors (first_name, second_name, birth_date) VALUES (?, ?, ?)";
    //language=TSQL
    String UPDATE_AUTHOR_BY_ID = "UPDATE Authors SET first_name = ?, second_name = ?, birth_date=? WHERE id = ?";

    String FIND_ALL_AUTHORS_BOOKS = "SELECT book_id FROM Authors_Books WHERE author_id = ?";

}

