package ru.kata.spring_boot_security_demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Исключение, выбрасываемое при ошибке создания пользователя.
 */
public class UserNotCreatedException extends RuntimeException {
    // Логгер для отслеживания исключений.
    private static final Logger logger = LoggerFactory.getLogger(UserNotCreatedException.class);

    /**
     * Конструктор с сообщением об ошибке.
     * @param message Сообщение об ошибке.
     */
    public UserNotCreatedException(String message) {
        super(message);
        logger.error("UserNotCreatedException: {}", message);
    }
}
