package ru.kata.spring_boot_security_demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Класс-обработчик успешной аутентификации пользователя.
 * Перенаправляет пользователя в зависимости от его роли после логина.
 */
@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    // Логгер для отслеживания событий аутентификации.
    private static final Logger logger = LoggerFactory.getLogger(SuccessUserHandler.class);

    /**
     * Метод вызывается при успешной аутентификации.
     * Проверяет роли пользователя и перенаправляет на соответствующий URL.
     * @param httpServletRequest Запрос от клиента.
     * @param httpServletResponse Ответ сервера.
     * @param authentication Объект аутентификации с данными пользователя.
     * @throws IOException Если возникает ошибка ввода-вывода.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        logger.info("Успешная аутентификация для пользователя: {}", authentication.getName());

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            logger.debug("Перенаправление админа на /admin/users");
            httpServletResponse.sendRedirect("/admin/users");

        } else if (roles.contains("ROLE_USER")) {
            logger.debug("Перенаправление пользователя на /user");
            httpServletResponse.sendRedirect("/user");

        } else {
            logger.warn("Неизвестная роль, перенаправление на /login");
            httpServletResponse.sendRedirect("/login");
        }
    }
}