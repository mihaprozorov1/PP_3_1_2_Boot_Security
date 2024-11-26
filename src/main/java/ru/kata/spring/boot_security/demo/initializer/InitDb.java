package ru.kata.spring.boot_security.demo.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;

@Component
public class InitDb {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public InitDb(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {

        // Создаем роли
        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        roleRepository.save(userRole);

        // Создаем пользователей
        User admin = new User("admin", "admin@example.com", "$2a$12$KUWvPmLUso.yacf.vsfWTOLt9r8GXjkFlNUio4Dt7ISxoHeHVQHAe");
        admin.setRoles(new HashSet<>(List.of(adminRole, userRole)));

        User user = new User("user", "user@example.com","$2a$12$KUWvPmLUso.yacf.vsfWTOLt9r8GXjkFlNUio4Dt7ISxoHeHVQHAe");
        user.setRoles(new HashSet<>(List.of(userRole)));

        // Сохраняем пользователей
        userRepository.save(admin);
        userRepository.save(user);

        System.out.println("Тестовые пользователи и роли успешно добавлены в БД.");
    }
}
