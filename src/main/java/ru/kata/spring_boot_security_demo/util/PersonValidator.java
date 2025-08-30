package ru.kata.spring_boot_security_demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring_boot_security_demo.models.User;
import ru.kata.spring_boot_security_demo.service.UserDetailsServiceImp;

/**
 * Валидатор для проверки уникальности email пользователя.
 */
@Component
public class PersonValidator implements Validator {

    // Логгер для отслеживания операций валидации.
    private static final Logger logger = LoggerFactory.getLogger(PersonValidator.class);

    private final UserDetailsServiceImp userDetailsServiceImp;

    /**
     * Конструктор для инъекции зависимостей.
     * @param userDetailsServiceImp Сервис для проверки пользователей.
     */
    @Autowired
    public PersonValidator(UserDetailsServiceImp userDetailsServiceImp) {
        logger.info("Инициализация PersonValidator");
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    /**
     * Проверяет, поддерживает ли валидатор указанный класс.
     * @param clazz Класс для проверки.
     * @return true, если класс User.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        boolean supported = User.class.equals(clazz);
        logger.debug("Проверка поддержки класса {}: {}", clazz.getSimpleName(), supported);
        return supported;
    }

    /**
     * Проверяет уникальность email пользователя.
     * @param target Объект для валидации (User).
     * @param errors Объект для записи ошибок.
     */
    @Override
    public void validate(Object target, Errors errors) {
        User person = (User) target;
        logger.info("Валидация пользователя с email: {}", person.getEmail());
        try {
            userDetailsServiceImp.loadUserByUsername(person.getEmail());
            errors.rejectValue("email", "Invalid email address", "Email уже занят");
            logger.warn("Email {} уже занят", person.getEmail());
        } catch (UsernameNotFoundException ignored) {
            logger.debug("Email {} свободен", person.getEmail());
        }
    }
}
