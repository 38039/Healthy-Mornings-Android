package com.nforge.healthymornings.model;

import java.sql.Date;

public class UserData {
    private String name = null;
    private String surname = null;
    private String gender = null;
    private String username = null;
    private String email = null;
    private String bio = null;
    private Date birthDate = null;
    private double height = 0;
    private double weight = 0;
    private boolean isAdmin = false;

    public UserData(
            String name,
            String surname,
            String gender,
            String username,
            String email,
            String bio,
            Date birthDate,
            double height,
            double weight,
            boolean isAdmin
    ) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.isAdmin = isAdmin;
    }


    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
}
