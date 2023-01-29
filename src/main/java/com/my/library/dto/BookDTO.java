package com.my.library.dto;

import com.my.library.entities.Author;

import java.time.LocalDate;

public class BookDTO {
    private Long bookId;
    private String title;
    private String publisherTitle;
    private String genre;
    private int pageNumber;
    private LocalDate publicationDate;
    private String authorFirstName;
    private String authorSecondName;

    private boolean isRemoved;
    private int copies;

    public BookDTO(Long bookId, String title, String publisherTitle, String genre, int pageNumber, LocalDate publicationDate, String authorFirstName, String authorSecondName, boolean isRemoved, int copies) {
        this.bookId = bookId;
        this.title = title;
        this.publisherTitle = publisherTitle;
        this.genre = genre;
        this.pageNumber = pageNumber;
        this.publicationDate = publicationDate;
        this.authorFirstName = authorFirstName;
        this.authorSecondName = authorSecondName;
        this.isRemoved = isRemoved;
        this.copies = copies;
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

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorSecondName() {
        return authorSecondName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setAuthorSecondName(String authorSecondName) {
        this.authorSecondName = authorSecondName;
    }

    public boolean getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(boolean removed) {
        isRemoved = removed;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", publisherTitle='" + publisherTitle + '\'' +
                ", genre='" + genre + '\'' +
                ", pageNumber=" + pageNumber +
                ", publicationDate=" + publicationDate +
                ", authorFirstName='" + authorFirstName + '\'' +
                ", authorSecondName='" + authorSecondName + '\'' +
                ", isRemoved=" + isRemoved +
                ", copies=" + copies +
                '}';
    }
}
