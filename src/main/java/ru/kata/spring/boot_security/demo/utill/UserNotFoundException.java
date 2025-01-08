package ru.kata.spring.boot_security.demo.utill;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
