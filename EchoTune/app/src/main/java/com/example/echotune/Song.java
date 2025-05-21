package com.example.echotune;

import java.io.Serializable;

public class Song implements Serializable {
    private long id;
    private String title;
    private String author;
    private String genre;
    private String path;
    private int replays;

    public Song(long id, String title, String author, String genre, String path, int replays) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.path = path;
        this.replays = replays;
    }

    @Override
    public String toString(){
        return this.title+" "+this.replays;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public int getReplays() {
        return replays;
    }

    public void setReplays(int replays) {
        this.replays = replays;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setId(long id) {
        this.id = id;
    }
}
