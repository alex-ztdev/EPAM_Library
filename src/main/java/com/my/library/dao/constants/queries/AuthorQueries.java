package com.my.library.dao.constants.queries;

public interface AuthorQueries {
    String GET_AUTHOR_BY_ID = "SELECT * FROM Authors WHERE id = ?";
    String GET_ALL_AUTHORS = "SELECT * FROM Authors";
    String INSERT_AUTHOR = "INSERT INTO AUTHORS (first_name, second_name, birth_date) VALUES (?, ?, ?)";
    String DELETE_AUTHOR_BY_ID = "DELETE * FROM Authors WHERE id = ?";
}

