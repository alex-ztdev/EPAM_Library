package com.my.library.entities;

import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;

import java.time.LocalDate;

public class User {
    private Long userId;
    private String login;
    private String password;
    private UserRole role;
    private UserStatus status;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String secondName;
    private LocalDate birthDate;

    public User() {
    }

    public User(String login, String password, UserRole role, UserStatus status, String email, String phoneNumber, String firstName, String secondName, LocalDate birthDate) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
    }

    public User(Long userId, String login, String password, UserRole role, UserStatus status, String email, String phoneNumber, String firstName, String secondName, LocalDate birthDate) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.role = role;
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userId.equals(user.userId)) return false;
        if (!login.equals(user.login)) return false;
        if (!password.equals(user.password)) return false;
        if (role != user.role) return false;
        if (status != user.status) return false;
        if (!email.equals(user.email)) return false;
        if (!phoneNumber.equals(user.phoneNumber)) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (!secondName.equals(user.secondName)) return false;
        return birthDate.equals(user.birthDate);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        result = 31 * result + birthDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
