package com.my.library.dao;

import com.my.library.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> find(long id);

    List<User> findAll();

    void save(User entity);

    void update(User entity);

    void delete(User entity);
}
