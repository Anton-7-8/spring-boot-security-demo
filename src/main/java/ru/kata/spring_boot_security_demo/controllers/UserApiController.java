package ru.kata.spring_boot_security_demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring_boot_security_demo.models.User;
import ru.kata.spring_boot_security_demo.service.UserService;


/**
 * REST-контроллер для пользовательских операций.
 * Обрабатывает API-запросы для получения данных текущего пользователя.
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    // Логгер для отслеживания операций в контроллере.
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private final UserService userService;

    /**
     * Конструктор для инъекции зависимостей.
     * @param userService Сервис для работы с пользователями.
     */
    @Autowired
    public UserApiController(UserService userService) {
        logger.info("Инициализация UserApiController");
        this.userService = userService;
    }

    /**
     * Возвращает данные текущего пользователя с ролями.
     * @param user Текущий аутентифицированный пользователь.
     * @return Ответ с данными пользователя.
     */
    @GetMapping("/current")
    @Transactional(readOnly = true)
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        logger.info("Запрос данных текущего пользователя: {}", user.getEmail());
        User fullUser = userService.getUserWithRoles(user.getId());
        logger.debug("Возвращен пользователь: {}", fullUser.getEmail());
        return ResponseEntity.ok(fullUser);
    }
}