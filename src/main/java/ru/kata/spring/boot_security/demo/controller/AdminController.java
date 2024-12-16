package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
//    @Controller: Аннотация Spring, указывающая, что данный класс является контроллером MVC (Model-View-Controller) и обрабатывает HTTP-запросы.
//    @RequestMapping("/admin"): Базовый URL для всех методов контроллера. Все запросы к /admin и его под

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    //	Все Юзеры
    @GetMapping({"", "/"})
    public String getAllUser(Model model) {
        List<User> userList = userService.listUsers();
        model.addAttribute("users", userList);
        return "2_admin-page";//return "users";
    }
    //@GetMapping({"", "/"}) или `//admin: Обрабатывает GET-запросы к /admin/.
    //Model model: Используется для передачи данных из контроллера в представление.
    //userService.listUsers(): Получает список всех пользователей.
    //model.addAttribute("users", userList): Добавляет список пользователей в users.
    //return "users";: Возвращает users.

    // Выбрать юзера по ID
    @GetMapping("/{id}") //Обрабатывает GET-запросы с параметром id (например, /admin/1).
    public String show(@RequestParam("id") int id, Model model) { //Извлекает параметр id из запроса.
        model.addAttribute("user", userService.getById(id));
        return "show";
    }
    //@GetMapping("/{id}"): Обрабатывает GET-запросы с параметром id (например, /admin/1).
    //@RequestParam("id"): Извлекает параметр id из запроса.
    //userService.getById(id): Получает пользователя по его ID.
    //model.addAttribute("user", ...): Добавляет пользователя в модель для отображения в представлении.

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
    @PostMapping("/3_add-new-user")
    public String create(@ModelAttribute("user") User user, @RequestParam("role")String roleName) {
        Role role = userService.findRoleByName(roleName);
        user.setRoles(Set.of(role));// Устанавливаем новую роль
        userService.save(user);// Сохраняем изменения
        return "redirect:/admin";
    }
    //@PostMapping("/new"): Обрабатывает POST-запросы к /admin/new.
    //@ModelAttribute("user") User user: Извлекает данные из формы и связывает их с объектом User.
    //@RequestParam("role"): Извлекает выбранную роль из формы.
    //userService.findRoleByName(roleName): Получает объект роли по её имени.
    //user.setRoles(Set.of(role)): Устанавливает выбранную роль для пользователя.
    //userService.save(user): Сохраняет нового пользователя.
    //return "redirect:/admin";: Перенаправляет пользователя обратно на страницу /admin.

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
    public String update(@RequestParam("id") int id,
                         @ModelAttribute("user") User user,
                         @RequestParam("role") String roleName) {
        Role role = userService.findRoleByName(roleName); // Находим роль по имени
        user.setRoles(Set.of(role)); // Присваиваем выбранную роль пользователю
        userService.edit(user); // Обновляем данные пользователя
        return "redirect:/admin";
    }
    //@PostMapping("/edit"): Обрабатывает POST-запросы к /admin/edit.
    //@ModelAttribute("user") User user: Извлекает данные из формы и связывает их с объектом User.
    //userService.edit(user): Обновляет данные пользователя.
    //return "redirect:/admin";: Перенаправляет пользователя обратно на страницу /admin.

    //   Удалить Юзера
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteUser(@RequestParam("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
    //@RequestMapping(value = "/delete", method = RequestMethod.GET): Обрабатывает GET-запросы к /admin/delete.
    //@RequestParam("id") int id: Извлекает параметр id из запроса.
    //userService.delete(id): Удаляет пользователя по его ID.
    //return "redirect:/admin";: Перенаправляет пользователя обратно на страницу /admin.

//    @GetMapping("/3_add-new-user")
//    public String shouUserInfo2(Model model, @AuthenticationPrincipal UserDetails currentUser) {
//        User user = userService.getInfoByUser(currentUser.getUsername());
//        model.addAttribute("currentUser", user);
//        return "3_add-new-user";
//    }
}
