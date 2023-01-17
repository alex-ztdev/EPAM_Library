package com.my.library.entities;

import java.time.LocalDate;
import java.util.List;

public class Author {
    private Long authorId;
    private String firstName;
    private String secondName;

    private LocalDate birthDate;

    public Author() {
    }

    public Author(Long authorId, String firstName, String secondName, LocalDate birthDate) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
    }

    public Author(String firstName, String secondName, LocalDate birthDate) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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

        Author author = (Author) o;

        if (!authorId.equals(author.authorId)) return false;
        if (!firstName.equals(author.firstName)) return false;
        if (!secondName.equals(author.secondName)) return false;
        return birthDate.equals(author.birthDate);
    }

    @Override
    public int hashCode() {
        int result = authorId.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        result = 31 * result + birthDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
