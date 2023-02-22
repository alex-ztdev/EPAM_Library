package com.my.library.dto;

import com.my.library.entities.Author;

import java.time.LocalDate;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookDTO bookDTO = (BookDTO) o;

        if (pageNumber != bookDTO.pageNumber) return false;
        if (isRemoved != bookDTO.isRemoved) return false;
        if (copies != bookDTO.copies) return false;
        if (!Objects.equals(bookId, bookDTO.bookId)) return false;
        if (!Objects.equals(title, bookDTO.title)) return false;
        if (!Objects.equals(publisherTitle, bookDTO.publisherTitle))
            return false;
        if (!Objects.equals(genre, bookDTO.genre)) return false;
        if (!Objects.equals(publicationDate, bookDTO.publicationDate))
            return false;
        if (!Objects.equals(authorFirstName, bookDTO.authorFirstName))
            return false;
        return Objects.equals(authorSecondName, bookDTO.authorSecondName);
    }

    @Override
    public int hashCode() {
        int result = bookId != null ? bookId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (publisherTitle != null ? publisherTitle.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + pageNumber;
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        result = 31 * result + (authorFirstName != null ? authorFirstName.hashCode() : 0);
        result = 31 * result + (authorSecondName != null ? authorSecondName.hashCode() : 0);
        result = 31 * result + (isRemoved ? 1 : 0);
        result = 31 * result + copies;
        return result;
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
