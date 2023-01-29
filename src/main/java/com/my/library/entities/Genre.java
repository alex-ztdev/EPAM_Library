package com.my.library.entities;

public class Genre {
    private Long genreId;
    private String title;

    public Genre(Long genreId, String title) {
        this.genreId = genreId;
        this.title = title;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreId=" + genreId +
                ", title='" + title + '\'' +
                '}';
    }
}
