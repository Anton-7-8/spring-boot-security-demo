package ru.kata.spring_boot_security_demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений для API.
 */
@ControllerAdvice
public class UserGlobalExceptionHandler {

    // Логгер для отслеживания обработки исключений.
    private static final Logger logger = LoggerFactory.getLogger(UserGlobalExceptionHandler.class);

    /**
     * Обрабатывает исключение UserNotFoundException.
     * @param e Исключение.
     * @return Ответ с ошибкой 404.
     */
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotFoundException e) {
        logger.error("Обработка UserNotFoundException: {}", e.getMessage());
        UserErrorResponse error = new UserErrorResponse("User not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение UserNotCreatedException.
     * @param e Исключение.
     * @return Ответ с ошибкой 400.
     */
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        logger.error("Обработка UserNotCreatedException: {}", e.getMessage());
        UserErrorResponse error = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение UserNotEditException.
     * @param e Исключение.
     * @return Ответ с ошибкой 404.
     */
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotEditException e) {
        logger.error("Обработка UserNotEditException: {}", e.getMessage());
        UserErrorResponse error = new UserErrorResponse("User not edit");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает общие исключения.
     * @param e Исключение.
     * @return Ответ с ошибкой 404.
     */
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(Exception e) {
        logger.error("Обработка общего исключения: {}", e.getMessage());
        UserErrorResponse error = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
