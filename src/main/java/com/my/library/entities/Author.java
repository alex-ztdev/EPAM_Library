package com.my.library.entities;

import java.time.LocalDate;
import java.util.List;

public class Author {
    public static final Author UNKNOWN_AUTHOR = new Author("Unknown", "Unknown");
    private Long authorId;
    private String firstName;
    private String secondName;

    private Author(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = firstName;
    }

    public Author(Long authorId, String firstName, String secondName) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.secondName = secondName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (authorId != null ? !authorId.equals(author.authorId) : author.authorId != null) return false;
        if (firstName != null ? !firstName.equals(author.firstName) : author.firstName != null) return false;
        return secondName != null ? secondName.equals(author.secondName) : author.secondName == null;
    }

    @Override
    public int hashCode() {
        int result = authorId != null ? authorId.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
