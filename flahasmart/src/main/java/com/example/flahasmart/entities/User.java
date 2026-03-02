package com.example.flahasmart.entities;

public class User {
    private int id;
    private String email;
    // constructeurs, getters, setters
    public User(int id, String email) { this.id = id; this.email = email; }
    public int getId() { return id; }
    public String getEmail() { return email; }
}