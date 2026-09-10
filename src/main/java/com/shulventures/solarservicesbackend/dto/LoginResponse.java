package com.shulventures.solarservicesbackend.dto;

public class LoginResponse {

    private boolean success;
    private String message;
    private Long id;
    private String name;
    private String email;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String message, Long id,
                         String name,String email) {
        this.success = success;
        this.message = message;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}