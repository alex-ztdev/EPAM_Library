package com.my.library.dao.constants.queries;

public interface AuthorQueries {
    //language=TSQL
    String FIND_AUTHOR_BY_ID = "SELECT id as author_id, first_name, second_name FROM Authors WHERE id = ?";
    //language=TSQL
    String FIND_ALL_AUTHORS = "SELECT id as author_id, first_name, second_name FROM Authors";
    //language=TSQL
    String INSERT_AUTHOR = "INSERT INTO Authors (first_name, second_name) VALUES (?, ?)";
    //language=TSQL
    String UPDATE_AUTHOR_BY_ID = "UPDATE Authors SET first_name = ?, second_name = ? WHERE id = ?";

    //language=TSQL
    String FIND_ALL_AUTHORS_BOOKS = "SELECT id FROM BOOKS WHERE author_id = ?";

}

