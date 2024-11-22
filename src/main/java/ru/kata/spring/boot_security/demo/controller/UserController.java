package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserServiceImpl userService;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserServiceImpl userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @GetMapping()
    public String shouUserInfo(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = (User) userService.getInfoByUser(currentUser.getUsername());
        model.addAttribute("currentUser", user);
        return "shouUserInfo";
    }
}
