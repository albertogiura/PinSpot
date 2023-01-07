package com.blackbox.pinspot.model;

import java.util.Objects;

public class Pin {
    private LatitudeLongitude position;
    private String title, link;
    private int likes;
    // to do aggiungere linke altre robe utili
    // constructors

    public Pin() {
       /* this.position = new LatitudeLongitude(0.0, 0.0);
        this.title = "";
        this.link = "";
        this.likes = 0;*/
    }

    public Pin(LatitudeLongitude position, String title, String link, int likes) {
        this.position = position;
        this.title = title;
        this.link = link;
        this.likes = likes;
    }

    // toString() & hashCode() & equals()

    @Override
    public String toString() {
        return "Pin{" +
                "position=" + position.toString() +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", likes=" + likes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pin pin = (Pin) o;
        return likes == pin.likes && Objects.equals(position, pin.position) && Objects.equals(title, pin.title) && Objects.equals(link, pin.link);
    }

    // getter and setter

    @Override
    public int hashCode() {
        return Objects.hash(position, title, link, likes);
    }

    public LatitudeLongitude getPosition() {
        return position;
    }

    public void setPosition(LatitudeLongitude position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
