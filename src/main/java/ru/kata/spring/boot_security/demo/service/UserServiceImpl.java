package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Transactional
    @Override
    public void edit(User user) {
        userRepository.update(user.getUsername(), user.getEmail(), user.getId());
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Role findRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() ->
                new IllegalArgumentException("Role not found: " + roleName));
    }

    //Этот метод ищет пользователя по имени с помощью userRepository и возвращает объект User, если пользователь найден.
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    //метод из интерфейса UserDetailsService для сравнения полученого имя пользователя с хранящимся в базе
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return Optional.ofNullable(findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }

    public User getInfoByUser(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }
}

