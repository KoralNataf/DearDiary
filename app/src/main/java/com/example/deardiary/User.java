package com.example.deardiary;

import android.net.Uri;

public class User {

    private String id;
    private String userLastName;
    private String username;
    private String email;
    private String imageURL;


    public User() {
    }

    public User(String id, String userLastName, String username, String email, String imageURL) {
        this.id = id;
        this.userLastName = userLastName;
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}