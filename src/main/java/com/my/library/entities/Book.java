package com.my.library.entities;


import java.time.LocalDate;

public class Book {
    private Long bookId;
    private String title;
    private String publisherTitle;
    private String genre;
    private String pageNumber;
    private LocalDate publicationDate;
    private boolean isAvailable;

    public Book() {
    }

    public Book(String title, String publisherTitle, String genre, String pageNumber, LocalDate publicationDate, boolean isAvailable) {
        this.title = title;
        this.publisherTitle = publisherTitle;
        this.genre = genre;
        this.pageNumber = pageNumber;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
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

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
