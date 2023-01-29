package com.my.library.entities;

public class Publisher {
    private Long publisherId;
    private String title;

    public Publisher(String title) {
        this.title = title;
    }

    public Publisher(Long publisherId, String title) {
        this.publisherId = publisherId;
        this.title = title;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "publisherId=" + publisherId +
                ", title='" + title + '\'' +
                '}';
    }
}
