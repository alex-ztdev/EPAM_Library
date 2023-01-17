package com.my.library.dao;

import com.my.library.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> find(long id);

    List<User> findAll();

    void save(User entity);

    boolean update(User entity);

    void block(User entity);
}
