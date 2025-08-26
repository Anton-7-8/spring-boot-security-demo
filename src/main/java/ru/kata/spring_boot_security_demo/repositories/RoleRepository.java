package ru.kata.spring_boot_security_demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring_boot_security_demo.models.Role;

/**
 * Репозиторий для работы с ролями.
 * Расширяет JpaRepository для стандартных операций с базой данных.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по имени.
     * @param roleName Имя роли.
     * @return Объект роли.
     */
    Role findByRoleName(String roleName);
}