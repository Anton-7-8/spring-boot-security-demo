package ru.kata.spring_boot_security_demo.unit;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring_boot_security_demo.models.Role;
import ru.kata.spring_boot_security_demo.models.User;
import ru.kata.spring_boot_security_demo.service.RoleService;
import ru.kata.spring_boot_security_demo.service.UserService;

import java.util.Collection;
import java.util.HashSet;

/**
 * Класс для инициализации базы данных тестовыми данными.
 */
@Component
public class DbInit {

    // Логгер для отслеживания операций инициализации.
    private static final Logger logger = LoggerFactory.getLogger(DbInit.class);

    private final UserService userService;
    private final RoleService roleService;

    /**
     * Конструктор для инъекции зависимостей.
     * @param userService Сервис пользователей.
     * @param roleService Сервис ролей.
     */
    @Autowired
    public DbInit(UserService userService, RoleService roleService) {
        logger.info("Инициализация DbInit");
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * Инициализирует базу данных тестовыми ролями и пользователями.
     */
    @PostConstruct
    private void postConstruct() {
        logger.info("Начало инициализации тестовых данных");
        roleService.save(new Role("ROLE_USER"));
        logger.debug("Сохранена роль: ROLE_USER");
        roleService.save(new Role("ROLE_ADMIN"));
        logger.debug("Сохранена роль: ROLE_ADMIN");

        Collection<Role> roleAdmin = new HashSet<>();
        Collection<Role> roleUser = new HashSet<>();
        Role adminRole = roleService.findByRoleName("ROLE_ADMIN");
        Role userRole = roleService.findByRoleName("ROLE_USER");
        roleAdmin.add(adminRole);
        roleUser.add(userRole);
        logger.debug("Найдены роли: {} и {}", adminRole, userRole);

        User admin = new User();
        admin.setName("admin");
        admin.setLastname("admin");
        admin.setRole(roleAdmin);
        admin.setAge(30);
        admin.setEmail("admin@mail.ru");
        admin.setPassword("admin");
        userService.addNewUser(admin);
        logger.debug("Добавлен пользователь: {}", admin.getEmail());

        User user = new User();
        user.setName("user");
        user.setLastname("user");
        user.setRole(roleUser);
        user.setAge(20);
        user.setEmail("user@mail.ru");
        user.setPassword("user");
        userService.addNewUser(user);
        logger.debug("Добавлен пользователь: {}", user.getEmail());
        logger.info("Инициализация тестовых данных завершена");
    }
}
