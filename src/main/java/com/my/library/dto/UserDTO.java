package com.my.library.dto;

import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;

public class UserDTO {
    private Long userId;
    private String login;
    private UserRole role;
    private UserStatus status;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String secondName;

    public UserDTO(Long userId, String login, UserRole role, UserStatus status, String email, String phoneNumber, String firstName, String secondName) {
        this.userId = userId;
        this.login = login;
        this.role = role;
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
