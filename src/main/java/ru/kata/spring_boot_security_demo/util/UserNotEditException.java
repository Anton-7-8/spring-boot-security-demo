package ru.kata.spring_boot_security_demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Исключение, выбрасываемое при ошибке редактирования пользователя.
 */
public class UserNotEditException extends RuntimeException {

    // Логгер для отслеживания исключений.
    private static final Logger logger = LoggerFactory.getLogger(UserNotEditException.class);

    /**
     * Конструктор с сообщением об ошибке.
     * @param message Сообщение об ошибке.
     */
    public UserNotEditException(String message) {
        super(message);
        logger.error("UserNotEditException: {}", message);
    }
}