package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    //	Все Юзеры
    @GetMapping({"", "/"})
    public String getAllUser(Model model) {
        List<User> userList = userServiceImpl.listUsers();
        model.addAttribute("users", userList);
        return "users";
    }

    // Выбрать юзера по ID
    @GetMapping("/{id}")
    public String show(@RequestParam("id") int id, Model model) {
        model.addAttribute("user", userServiceImpl.getById(id));
        return "show";
    }

    //	Добавить Юзера
    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        userServiceImpl.save(user);
        return "redirect:/admin";
    }

    //  Изменить Юзера
    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userServiceImpl.getById(id));
        return "edit";
    }

    @PostMapping("/edit")
    public String update(@RequestParam("id") int id, @ModelAttribute("user") User user) {
        userServiceImpl.edit(user);
        return "redirect:/admin";
    }

    //   Удалить Юзера
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteUser(@RequestParam("id") int id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }
}
