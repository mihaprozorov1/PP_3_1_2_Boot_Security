package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.utill.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.utill.UserNotFoundException;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8080")
@RestController //@Controller + @ResponseBody над каждым методом
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    //	Вывести всех Юзеров
    @GetMapping({"", "/"})
    public List<UserDTO> getAllUser(Model model) {
        return userService.listUsers().stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());//return "users";
    }

    //	Вывести Юзера по ID
    @GetMapping("/{id}") //Обрабатывает GET-запросы с параметром id (например, /admin/1).
    public UserDTO show(@PathVariable("id") int id) { //Извлекает параметр id из запроса.
        if (userService.getById(id) == null) {
            throw new UserNotFoundException("User с таким ID = " + id + " в базе-данных нет");
        }
        return convertToUserDTO(userService.getById(id));
    }

    //	Добавить Юзера
    @PostMapping("/")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult, @RequestParam("role") String roleName) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMsg = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorsMsg.append(fieldError.getField()).append(" :!!!!!!!!! ").append(fieldError.getDefaultMessage()).append("\n");
            }
            throw new UserNotCreatedException(errorsMsg.toString());
        }
        Role role = userService.findRoleByName(roleName);
        userDTO.setRoles(Set.of(role));// Устанавливаем новую роль
        userService.save(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //      Изменить Юзера
    @PutMapping("/")
    public ResponseEntity<UserDTO> update1(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult, @RequestParam("role") String roleName) {
        // Проверяем ошибки валидации
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMsg = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorsMsg.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append("\n");
            }
            throw new UserNotCreatedException(errorsMsg.toString());
        }

        // Проверяем, существует ли пользователь
        User existingUser = userService.getById(userDTO.getId());
        if (existingUser == null) {
            throw new UserNotFoundException("User с ID = " + userDTO.getId() + " не найден");
        }

        // Устанавливаем новые данные пользователя
        Role roleEntity = userService.findRoleByName(roleName);
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setAge(userDTO.getAge());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setRoles(new HashSet<>(Set.of(roleEntity)));

        // Сохраняем изменения
        userService.edit(existingUser);

        // Возвращаем обновленные данные
        return ResponseEntity.ok(convertToUserDTO(existingUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        // Проверяем, существует ли пользователь
        if (userService.getById(id) == null) {
            throw new UserNotFoundException("User с таким ID = " + id + " в базе-данных нет");
        }
        // Удаляем пользователя
        userService.delete(id);
        // Возвращаем подтверждение с HTTP статусом 200 OK
        return ResponseEntity.ok("User with ID = " + id + " deleted");
    }

    //Вывести инфо о авторизованном юзере
    @PreAuthorize("permitAll()")
    @GetMapping("infoByThisUser")
    public ResponseEntity<UserDTO> show() {
        // Получаем текущего авторизованного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем имя пользователя из Authentication
        System.out.println(email);
        // Получаем информацию о пользователе по имени
        User user = userService.getInfoByUser(email);
        if (user == null) {
            throw new UserNotFoundException("User с email = " + email + " в базе-данных не найден");
        }
        return ResponseEntity.ok(convertToUserDTO(user)); // Преобразуем User в UserDTO и возвращаем с кодом 200 OK
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
