package com.library.dao;

import com.library.entities.Author;
import com.library.entities.Book;

import java.util.List;
import java.util.Optional;

public interface AuthorDAO {
    Optional<Author> find(long id);

    List<Author> findAll();

    Author save(Author entity);

    void update(Author entity);

    void delete(Author entity);
}
