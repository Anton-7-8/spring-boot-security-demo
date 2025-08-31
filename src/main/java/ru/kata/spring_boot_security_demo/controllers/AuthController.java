package ru.kata.spring_boot_security_demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для обработки страницы аутентификации.
 * Обрабатывает запросы к странице логина.
 */
@Controller
public class AuthController {
    // Логгер для отслеживания операций в контроллере.
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Отображает страницу логина.
     * @return Название шаблона для страницы логина (auth/login).
     */
    @RequestMapping("/login")
    public String login() {
        logger.info("Запрос страницы логина");
        return "auth/login";
    }
}
