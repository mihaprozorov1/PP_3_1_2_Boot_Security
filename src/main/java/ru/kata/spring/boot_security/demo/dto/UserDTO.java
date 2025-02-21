package ru.kata.spring.boot_security.demo.dto;

import lombok.Data;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String username;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String lastName;

    private Integer age;

    @Email
    private String email;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String password;

    private Set<Role> roles;
}

