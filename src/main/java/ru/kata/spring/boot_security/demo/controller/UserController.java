package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Тестовый метод без @AuthenticationPrincipal - проверки на авторизацию
    @GetMapping()
    public ResponseEntity<?> shouUserInfo() {
        // Тестовый сценарий без аутентификации
        String testUsername = "user@example.com";

        User user = (User) userService.getInfoByUser(testUsername);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setLastName(user.getLastName());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return ResponseEntity.ok(userDTO);
    }

    //    @GetMapping()
//    public ResponseEntity<?> shouUserInfo(@AuthenticationPrincipal UserDetails currentUser) {
//        if (currentUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
//        }
//
//        User user = (User) userService.getInfoByUser(currentUser.getUsername());
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(user.getId());
//        userDTO.setUsername(user.getUsername());
//        userDTO.setLastName(user.getLastName());
//        userDTO.setAge(user.getAge());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setRoles(user.getRoles());
//        return ResponseEntity.ok(userDTO);
//    }
}
