package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.utill.UserErrorResponse;
import ru.kata.spring.boot_security.demo.utill.UserNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController //@Controller + @ResponseBody над каждым методом
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping({"", "/"})
    public  List<User> getAllUser(Model model) {
        return userService.listUsers();//return "users";
    }

    @GetMapping("/getById") //Обрабатывает GET-запросы с параметром id (например, /admin/1).
    public User show(@RequestParam("id") int id) { //Извлекает параметр id из запроса.
        if (userService.getById(id) == null) {
            throw new UserNotFoundException("User с таким ID = " + id + " в базе-данных нет" );
        }
        return userService.getById(id);
    }

    //	Добавить Юзера
    @GetMapping("/3_add-new-user")
//    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "3_add-new-user";
//         return "new";
    }
    //@GetMapping("/new"): Обрабатывает GET-запрос к /admin/new.
    //@ModelAttribute("user") User user: Создаёт пустой объект User и добавляет его в модель под именем user.
    //return "new";: Возвращает имя представления new.

    //    @PostMapping("/new")
//    @PostMapping("/3_add-new-user")
//    public String create(@ModelAttribute("user") User user, @RequestParam("role") String roleName) {
//        Role role = userService.findRoleByName(roleName);
//        user.setRoles(Set.of(role));// Устанавливаем новую роль
//        userService.save(user);// Сохраняем изменения
//        return "redirect:/admin";
//    }
    //@PostMapping("/new"): Обрабатывает POST-запросы к /admin/new.
    //@ModelAttribute("user") User user: Извлекает данные из формы и связывает их с объектом User.
    //@RequestParam("role"): Извлекает выбранную роль из формы.
    //userService.findRoleByName(roleName): Получает объект роли по её имени.
    //user.setRoles(Set.of(role)): Устанавливает выбранную роль для пользователя.
    //userService.save(user): Сохраняет нового пользователя.
    //return "redirect:/admin";: Перенаправляет пользователя обратно на страницу /admin.

    @PostMapping("/3_add-new-user")
    public String create(@ModelAttribute("user") User user, @RequestParam("role") String roleName) {
        Role role = userService.findRoleByName(roleName);
        user.setRoles(Set.of(role));// Устанавливаем новую роль
        userService.save(user);// Сохраняем изменения
        return "redirect:/admin";
    }
    //  Изменить Юзера
    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("roles", userService.getAllRoles());
        return "edit";
    }
    //@GetMapping("/edit"): Обрабатывает GET-запросы к /admin/edit.
    //@RequestParam("id") int id: Извлекает параметр id из запроса.
    //model.addAttribute("user", userService.getById(id)): Добавляет пользователя с данным ID в модель.
    //return "edit";: Возвращает имя представления edit.

    @PostMapping("/edit")
    public String update(Long userId, String firstName, String lastName, Integer age, String email, String role) {
        Role roleEntity = userService.findRoleByName(role); // Находим роль по имени
        User user = new User();
        user.setId(userId);
        user.setUsername(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        user.setRoles(new HashSet<>(Set.of(roleEntity))); // Присваиваем выбранную роль пользователю
        userService.edit(user); // Обновляем данные пользователя
        return "redirect:/admin";
    }
    //@PostMapping("/edit"): Обрабатывает POST-запросы к /admin/edit.
    //@ModelAttribute("user") User user: Извлекает данные из формы и связывает их с объектом User.
    //userService.edit(user): Обновляет данные пользователя.
    //return "redirect:/admin";: Перенаправляет пользователя обратно на страницу /admin.

    //   Удалить Юзера
    @GetMapping( "/delete")
    public String deleteUser(@RequestParam("userId") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
    //@RequestMapping(value = "/delete", method = RequestMethod.GET): Обрабатывает GET-запросы к /admin/delete.
    //@RequestParam("id") int id: Извлекает параметр id из запроса.
    //userService.delete(id): Удаляет пользователя по его ID.
    //return "redirect:/admin";: Перенаправляет пользователя обратно на страницу /admin.

    @GetMapping("/7_user-information-page")
    public String show(Model model) {
        // Получаем текущего авторизованного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем имя пользователя из Authentication
        System.out.println(email);
        // Получаем информацию о пользователе по имени
        User user = userService.getInfoByUser(email);

        // Добавляем информацию о пользователе в модель
        model.addAttribute("currentUserByAdmin", user);

        return "7_user-information-page";
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handlerException (UserNotFoundException e){
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handlerException (Exception e){
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
