package com.example.my;

// This is the User model class for firebase
public class User {
    private String email;

    // Default constructor (necessary for Firestore)
    public User() {
        // Empty constructor for Firestore
    }

    // Constructor with parameters
    public User(String email) {
        this.email = email;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
