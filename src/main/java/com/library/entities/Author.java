package com.library.entities;

import java.time.LocalDate;

public class Author {
    private Long id;
    private String firstName;
    private String secondName;

    private LocalDate birthDate;

    public Author() {
    }

    public Author(String firstName, String secondName, LocalDate birthDate) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
    }

    public Author(Long id, String firstName, String secondName, LocalDate birthDate ) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
