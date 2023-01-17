package com.my.library.entities;

import java.time.LocalDate;
import java.util.List;

public class Author {
    private Long authorId;
    private String firstName;
    private String secondName;

    private LocalDate birthDate;

    private List<Book> bookList;



    public Author(String firstName, String secondName, LocalDate birthDate, List<Book> bookList) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.bookList = bookList;
    }

    public Author(Long authorId, String firstName, String secondName, LocalDate birthDate, List<Book> bookList) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.bookList = bookList;
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

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (!authorId.equals(author.authorId)) return false;
        if (!firstName.equals(author.firstName)) return false;
        if (!secondName.equals(author.secondName)) return false;
        if (!birthDate.equals(author.birthDate)) return false;
        return bookList.equals(author.bookList);
    }

    @Override
    public int hashCode() {
        int result = authorId.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        result = 31 * result + birthDate.hashCode();
        result = 31 * result + bookList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", birthDate=" + birthDate +
                ", bookList=" + bookList +
                '}';
    }
}
