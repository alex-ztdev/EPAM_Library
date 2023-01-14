package com.library.dao;

import com.library.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    Optional<Book> find(long id);

    List<Book> findAll();

    void save(Book t);

    void update(Book t);

    void delete(Book t);
}
