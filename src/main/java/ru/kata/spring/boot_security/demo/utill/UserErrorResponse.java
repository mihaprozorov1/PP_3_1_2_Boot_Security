package ru.kata.spring.boot_security.demo.utill;

public class UserErrorResponse {
    private String message;

    public UserErrorResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
