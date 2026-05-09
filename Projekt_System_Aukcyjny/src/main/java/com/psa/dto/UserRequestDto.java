package com.psa.dto;

import java.util.Objects;

public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    // Konstruktory
    public UserRequestDto() {}

    // Gettery i Settery
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}