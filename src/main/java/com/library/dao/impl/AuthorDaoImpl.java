package com.library.dao.impl;


import com.library.connection_pool.DBManager;
import com.library.dao.AuthorDAO;
import com.library.entities.Author;

import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl implements AuthorDAO {
    private static volatile AuthorDaoImpl INSTANCE;
    private static final DBManager dbm = DBManager.getInstance();

    private static final String URL = "db.url";

    private AuthorDaoImpl() {}

    public static AuthorDaoImpl getInstance(){
        AuthorDaoImpl instance = INSTANCE;
        if (instance != null) {
            return instance;
        }
        synchronized (AuthorDaoImpl.class) {
            if (instance == null) {
                instance = new AuthorDaoImpl();
            }
            return instance;
        }
    }

    @Override
    public Optional<Author> find(long id) {
        try(var connection dbm.)
    }

    @Override
    public List<Author> findAll() {
        return null;
    }

    @Override
    public void save(Author entity) {

    }

    @Override
    public void update(Author entity) {

    }

    @Override
    public void delete(Author entity) {

    }
}
