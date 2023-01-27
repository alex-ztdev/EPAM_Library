package com.my.library.entities;


import java.time.LocalDate;
import java.util.List;

public class Book {
    private Long bookId;
    private String title;
    private String publisherTitle;
    private String genre;
    private int pageNumber;
    private LocalDate publicationDate;

    private Author author;
    private boolean isAvailable;

    private boolean isRemoved;

    public Book(String title, String publisherTitle, String genre, int pageNumber, LocalDate publicationDate, Author author, boolean isAvailable, boolean isRemoved) {
        this.title = title;
        this.publisherTitle = publisherTitle;
        this.genre = genre;
        this.pageNumber = pageNumber;
        this.publicationDate = publicationDate;
        this.author = author;
        this.isAvailable = isAvailable;
        this.isRemoved = isRemoved;
    }

    public Book(Long bookId, String title, String publisherTitle, String genre, int pageNumber, LocalDate publicationDate, Author author, boolean isAvailable, boolean isRemoved) {
        this.bookId = bookId;
        this.title = title;
        this.publisherTitle = publisherTitle;
        this.genre = genre;
        this.pageNumber = pageNumber;
        this.publicationDate = publicationDate;
        this.author = author;
        this.isAvailable = isAvailable;
        this.isRemoved = isRemoved;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (pageNumber != book.pageNumber) return false;
        if (isAvailable != book.isAvailable) return false;
        if (isRemoved != book.isRemoved) return false;
        if (!bookId.equals(book.bookId)) return false;
        if (!title.equals(book.title)) return false;
        if (!publisherTitle.equals(book.publisherTitle)) return false;
        if (!genre.equals(book.genre)) return false;
        if (!publicationDate.equals(book.publicationDate)) return false;
        return author.equals(book.author);
    }

    @Override
    public int hashCode() {
        int result = bookId.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + publisherTitle.hashCode();
        result = 31 * result + genre.hashCode();
        result = 31 * result + pageNumber;
        result = 31 * result + publicationDate.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + (isAvailable ? 1 : 0);
        result = 31 * result + (isRemoved ? 1 : 0);
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
                ", isAvailable=" + isAvailable +
                ", isRemoved=" + isRemoved +
                '}';
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
