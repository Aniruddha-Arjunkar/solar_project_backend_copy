package com.shulventures.solarservicesbackend.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contact;
    private String email;
    private String password;

    // Default constructor
    public User() {
    }

    // Constructor
    public User(String name, String contact, String email, String password) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.password = password;
    }

    //==== Getters and Setters ID ====
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    //==== Getters and Setters Name ====
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //==== Getters and Setters Contact ====
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    //=== Getters and Setters Email ===
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //=== Getters and Setters Password ===
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}