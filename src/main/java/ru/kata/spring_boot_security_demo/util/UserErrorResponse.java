package ru.kata.spring_boot_security_demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс для представления ответа об ошибке.
 */
public class UserErrorResponse {

    // Логгер для отслеживания операций с объектом ошибки.
    private static final Logger logger = LoggerFactory.getLogger(UserErrorResponse.class);

    private String message;

    /**
     * Конструктор с сообщением об ошибке.
     * @param message Сообщение об ошибке.
     */
    public UserErrorResponse(String message) {
        logger.debug("Создание UserErrorResponse с сообщением: {}", message);
        this.message = message;
    }

    /**
     * Возвращает сообщение об ошибке.
     * @return Сообщение.
     */
    public String getMessage() {
        logger.debug("Получение сообщения: {}", message);
        return message;
    }

    /**
     * Устанавливает сообщение об ошибке.
     * @param message Сообщение.
     */
    public void setMessage(String message) {
        logger.debug("Установка сообщения: {}", message);
        this.message = message;
    }
}
