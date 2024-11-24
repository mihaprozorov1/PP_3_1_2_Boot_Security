package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    List<User> listUsers();

    void save(User user);

    User getById(long id);

    void edit(User user);

    void delete(long id);
}
