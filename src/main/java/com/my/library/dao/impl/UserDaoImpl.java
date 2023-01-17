package com.my.library.dao.impl;

import com.my.library.dao.UserDAO;
import com.my.library.entities.User;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDAO {

    @Override
    public Optional<User> find(long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void save(User entity) {

    }

    @Override
    public boolean update(User entity) {
        return false;
    }

    @Override
    public void block(User entity) {

    }
}
