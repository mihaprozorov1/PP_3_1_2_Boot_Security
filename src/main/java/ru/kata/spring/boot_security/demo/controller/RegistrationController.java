package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

//import javax.validation.Valid;
//
//@Controller
//public class RegistrationController {
//
//    private final UserServiceImpl userServiceImpl;
//
//    @Autowired
//    public RegistrationController(UserServiceImpl userServiceImpl) {
//        this.userServiceImpl = userServiceImpl;
//    }
//
//    @GetMapping("/registration")
//    public String registration(Model model) {
//        model.addAttribute("userForm", new User());
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
//
//        if (bindingResult.hasErrors()) {
//            return "registration";
//        }
//        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
//            model.addAttribute("passwordError", "Пароли не совпадают");
//            return "registration";
//        }
//        if (!userServiceImpl.saveUser(userForm)){
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
//            return "registration";
//        }
//
//        return "redirect:/";
//    }
//}