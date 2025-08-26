package ru.kata.spring_boot_security_demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring_boot_security_demo.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Расширяет JpaRepository для стандартных операций с базой данных.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по email с подгруженными ролями.
     * @param email Email пользователя.
     * @return Optional с пользователем.
     */
    @Query("Select u from User u left join fetch u.role where u.email=:email")
    Optional<User> findByEmail(String email);

    /**
     * Возвращает всех пользователей с подгруженными ролями.
     * @return Список пользователей.
     */

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.role")
    List<User> findAllWithRoles();

    /**
     * Находит пользователя по ID с подгруженными ролями.
     * @param id Идентификатор пользователя.
     * @return Пользователь.
     */

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.id = :id")
    User getUserWithRoles(@Param("id") Long id);
}
