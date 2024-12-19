package com.example.myapplication.models;

public class EventDTO {
    private int event_id;
    private String title;
    private String location;
    private String date;
    private String description;
    private String username;
    private String image;


    public EventDTO(int event_id, String title, String location, String date, String description, String image, String username) {
        this.event_id = event_id;
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.username = username;
        this.image = image;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
