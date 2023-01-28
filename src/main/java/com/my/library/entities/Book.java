package com.my.library.entities;


import java.time.LocalDate;

public class Book {
    private Long bookId;
    private String title;
    private String publisherTitle;
    private String genre;
    private int pageNumber;
    private LocalDate publicationDate;
    private Author author;


    public Book(String title, String publisherTitle, String genre, int pageNumber, LocalDate publicationDate, Author author) {
        this.title = title;
        this.publisherTitle = publisherTitle;
        this.genre = genre;
        this.pageNumber = pageNumber;
        this.publicationDate = publicationDate;
        this.author = author;

    }

    public Book(Long bookId, String title, String publisherTitle, String genre, int pageNumber, LocalDate publicationDate, Author author) {
        this.bookId = bookId;
        this.title = title;
        this.publisherTitle = publisherTitle;
        this.genre = genre;
        this.pageNumber = pageNumber;
        this.publicationDate = publicationDate;
        this.author = author;

    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisherTitle() {
        return publisherTitle;
    }

    public void setPublisherTitle(String publisherTitle) {
        this.publisherTitle = publisherTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (pageNumber != book.pageNumber) return false;
        if (bookId != null ? !bookId.equals(book.bookId) : book.bookId != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;
        if (publisherTitle != null ? !publisherTitle.equals(book.publisherTitle) : book.publisherTitle != null)
            return false;
        if (genre != null ? !genre.equals(book.genre) : book.genre != null) return false;
        if (publicationDate != null ? !publicationDate.equals(book.publicationDate) : book.publicationDate != null)
            return false;
        return author != null ? author.equals(book.author) : book.author == null;
    }

    @Override
    public int hashCode() {
        int result = bookId != null ? bookId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (publisherTitle != null ? publisherTitle.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + pageNumber;
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", publisherTitle='" + publisherTitle + '\'' +
                ", genre='" + genre + '\'' +
                ", pageNumber=" + pageNumber +
                ", publicationDate=" + publicationDate +
                ", author=" + author +
                '}';
    }
}
