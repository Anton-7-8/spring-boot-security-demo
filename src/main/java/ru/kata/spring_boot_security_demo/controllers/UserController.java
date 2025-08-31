package ru.kata.spring_boot_security_demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring_boot_security_demo.models.User;

/**
 * Контроллер для пользовательской страницы.
 * Обрабатывает запросы к странице профиля пользователя.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    // Логгер для отслеживания операций в контроллере.
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Отображает страницу профиля пользователя.
     * @param user Текущий аутентифицированный пользователь.
     * @param model Модель для передачи данных в представление.
     * @return Название шаблона для страницы пользователя (user).
     */
    public String user(@AuthenticationPrincipal User user, Model model) {
        logger.info("Запрос страницы профиля для пользователя: {}", user.getEmail());
        model.addAttribute("user", user);
        return "user";
    }
}
