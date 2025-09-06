package ru.kata.spring_boot_security_demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import ru.kata.spring_boot_security_demo.models.Role;
import ru.kata.spring_boot_security_demo.models.User;
import ru.kata.spring_boot_security_demo.service.RoleService;
import ru.kata.spring_boot_security_demo.service.UserService;
import ru.kata.spring_boot_security_demo.util.UserNotCreatedException;

import java.util.*;

/**
 * REST-контроллер для админских операций с пользователями.
 * Обрабатывает API-запросы для CRUD-операций.
 */
@RestController
@RequestMapping("/api/admin")
public class RESTController {

    // Логгер для отслеживания операций в контроллере.
    private static final Logger logger = LoggerFactory.getLogger(RESTController.class);

    private final UserService userService;
    private final RoleService roleService;

    /**
     * Конструктор для инъекции зависимостей.
     * @param userService Сервис для работы с пользователями.
     * @param roleService Сервис для работы с ролями.
     */
    @Autowired
    public RESTController(UserService userService, RoleService roleService) {
        logger.info("Инициализация RESTController");
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * Возвращает список всех пользователей.
     * @return Список пользователей в формате JSON.
     */
    @GetMapping()
    public List<User> getUsers() {
        logger.info("Запрос списка всех пользователей");
        return userService.getAllUsers();
    }

    /**
     * Возвращает пользователя по ID.
     * @param id Идентификатор пользователя.
     * @return Пользователь в формате JSON.
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public User getUser(@PathVariable("id") Long id) {
        logger.info("Запрос пользователя с ID: {}", id);
        User user = userService.getUserWithRoles(id);
        logger.debug("Возвращен пользователь: {}", user.getEmail());
        return user;
    }

    /**
     * Создает нового пользователя.
     * @param user Объект пользователя из запроса.
     * @param bindingResult Результаты валидации.
     * @return Статус ответа (OK при успехе).
     * @throws UserNotCreatedException Если есть ошибки валидации.
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        logger.info("Попытка создания пользователя: {}", user.getEmail());
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append(" "));
            logger.warn("Ошибки валидации при создании пользователя: {}", errors);
            throw new UserNotCreatedException(errors.toString());
        }
        userService.addNewUser(user);
        logger.info("Пользователь успешно создан: {}", user.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * Обновляет данные пользователя.
     * @param payload Данные пользователя из запроса.
     * @return Статус ответа (OK при успехе, BAD_REQUEST при ошибке).
     */
    @PutMapping
    @Transactional
    public ResponseEntity<HttpStatus> editUser(@RequestBody Map<String, Object> payload) {
        logger.info("Попытка обновления пользователя с ID: {}", payload.get("id"));
        try {
            Long id = Long.valueOf(payload.get("id").toString());
            User existingUser = userService.getUser(id);

            // Обновляем основные поля
            existingUser.setName((String) payload.get("name"));
            existingUser.setLastname((String) payload.get("lastname"));
            existingUser.setAge(Math.toIntExact(Long.valueOf(payload.get("age").toString())));
            existingUser.setEmail((String) payload.get("email"));

            // Обновляем пароль, если он изменился
            if (payload.containsKey("password")) {
                existingUser.setPassword((String) payload.get("password"));
                logger.debug("Пароль пользователя обновлен");
            }

            // Обновляем роли
            List<Map<String, Object>> roles = (List<Map<String, Object>>) payload.get("role");
            Set<Role> newRoles = new HashSet<>();
            for (Map<String, Object> roleMap : roles) {
                Long roleId = Long.valueOf(roleMap.get("id").toString());
                Optional<Role> roleOptional = roleService.findById(roleId);
                if (roleOptional.isPresent()) {
                    newRoles.add(roleOptional.get());
                } else {
                    logger.error("Роль с ID {} не найдена", roleId);
                    throw new RuntimeException("Role not found with id: " + roleId);
                }
            }
            existingUser.setRole(newRoles);
            userService.edit(existingUser);
            logger.info("Пользователь успешно обновлен: {}", existingUser.getEmail());
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Удаляет пользователя по ID.
     * @param id Идентификатор пользователя.
     * @return Статус ответа (OK при успехе).
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        logger.info("Удаление пользователя с ID: {}", id);
        userService.deleteUser(id);
        logger.info("Пользователь успешно удален: {}", id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}