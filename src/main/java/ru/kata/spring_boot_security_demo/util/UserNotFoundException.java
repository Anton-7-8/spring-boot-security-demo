package ru.kata.spring_boot_security_demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Исключение, выбрасываемое при отсутствии пользователя.
 */
public class UserNotFoundException extends RuntimeException {

    // Логгер для отслеживания исключений.
    private static final Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    /**
     * Конструктор по умолчанию.
     */
    public UserNotFoundException() {
        logger.error("UserNotFoundException без сообщения");
    }

    /**
     * Конструктор с сообщением об ошибке.
     * @param message Сообщение об ошибке.
     */
    public UserNotFoundException(String message) {
        super(message);
        logger.error("UserNotFoundException: {}", message);
    }
}
