package com.my.library.dto.mapper;

import com.my.library.dto.UserDTO;
import com.my.library.entities.User;

import java.util.List;

public class UserMapper {
    public UserDTO toDTO(User user) {
        return new UserDTO(user.getUserId(),
                user.getLogin(),
                user.getRole(),
                user.getStatus(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getSecondName()
        );
    }

    public List<UserDTO> toDTOList(List<User> usersList) {
        UserMapper mapper = new UserMapper();
        return usersList.stream().map(mapper::toDTO).toList();
    }
}
