package ru.kata.spring_boot_security_demo.service;

import org.springframework.stereotype.Component;
import ru.kata.spring_boot_security_demo.models.User;

import java.util.List;

@Component
public interface UserService {

    /**
     * Возвращает список всех пользователей.
     * @return Список пользователей.
     */
    List<User> getAllUsers();

    /**
     * Находит пользователя по ID.
     * @param id Идентификатор пользователя.
     * @return Пользователь.
     */
    User getUser(Long id);

    /**
     * Находит пользователя по ID с подгруженными ролями.
     * @param id Идентификатор пользователя.
     * @return Пользователь с ролями.
     */
    User getUserWithRoles(Long id);

    /**
     * Удаляет пользователя по ID.
     * @param id Идентификатор пользователя.
     */
    void deleteUser(Long id);

    /**
     * Добавляет нового пользователя.
     * @param user Пользователь для добавления.
     */
    void addNewUser(User user);

    /**
     * Редактирует существующего пользователя.
     * @param user Пользователь с обновленными данными.
     */
    void edit(User user);
}