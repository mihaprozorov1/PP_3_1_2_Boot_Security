package ru.kata.spring.boot_security.demo.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;

@Component
public class InitDb {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public InitDb(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
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
        User admin = new User("admin", "admin", 21, "admin@example.com", "$2a$12$KUWvPmLUso.yacf.vsfWTOLt9r8GXjkFlNUio4Dt7ISxoHeHVQHAe");
        admin.setRoles(new HashSet<>(List.of(adminRole, userRole)));

        User user = new User("user", "user", 32, "user@example.com", "$2a$12$KUWvPmLUso.yacf.vsfWTOLt9r8GXjkFlNUio4Dt7ISxoHeHVQHAe");
        user.setRoles(new HashSet<>(List.of(userRole)));

        // Сохраняем пользователей
        userService.save(admin);
        userService.save(user);

        System.out.println("Тестовые пользователи и роли успешно добавлены в БД.");
    }
}
