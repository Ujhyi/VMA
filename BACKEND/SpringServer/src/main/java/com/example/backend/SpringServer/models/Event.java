package com.example.backend.SpringServer.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    private int event_id;
    private String title;
    private String location;
    private String date;
    private String description;
    private byte[]  image;

    @Lob
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @ManyToOne
    @JoinColumn(name = "user_id") // Foreign key column name in the events table
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}



