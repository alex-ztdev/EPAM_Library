package com.my.library.dao.constants.queries;

public interface AuthorQueries {
    //language=TSQL
    String GET_AUTHOR_BY_ID = "SELECT * FROM Authors WHERE id = ?";
    //language=TSQL
    String GET_ALL_AUTHORS = "SELECT * FROM Authors";
    //language=TSQL
    String INSERT_AUTHOR = "INSERT INTO Authors (first_name, second_name, birth_date) VALUES (?, ?, ?)";
    //language=TSQL
    String UPDATE_AUTHOR_BY_ID = "UPDATE Authors SET first_name = ?, second_name = ?, birth_date=?";
}

