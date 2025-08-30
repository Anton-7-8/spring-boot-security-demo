package ru.kata.spring_boot_security_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring_boot_security_demo.models.Role;
import ru.kata.spring_boot_security_demo.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с ролями.
 */
@Service
public class RoleServiceImp implements RoleService {

    //Логгер для отслеживания операций в сервисе
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImp.class);

    /**
     * Конструктор для инъекции зависимостей.
     * @param roleRepository Репозиторий ролей.
     */
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        logger.info("Инициализация RoleServiceImp");
        this.roleRepository = roleRepository;
    }

    /**
     * Возвращает список всех ролей.
     * @return Список ролей.
     */
    @Override
    public List<Role> findAll() {
        logger.info("Запрос списка всех ролей");
        List<Role> roles = roleRepository.findAll();
        logger.debug("Найдено ролей: {}", roles.size());
        return roles;
    }

    /**
     * Находит роль по ID.
     * @param id Идентификатор роли.
     * @return Optional с ролью.
     */
    @Override
    public Optional<Role> findById(long id) {
        logger.info("Поиск роли с ID: {}", id);
        Optional<Role> role = roleRepository.findById(id);
        logger.debug("Результат поиска роли с ID {}: {}", id, role.isPresent() ? role.get() : "не найдена");
        return role;
    }

    /**
     * Находит роль по ID с обработкой исключения.
     * @param id Идентификатор роли.
     * @return Роль.
     * @throws RuntimeException Если роль не найдена.
     */
    @Override
    public Role getById(long id) {
        logger.info("Получение роли с ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Роль с ID {} не найдена", id);
                    return new RuntimeException("Role not found with id: " + id);
        });
        logger.debug("Роль найдена: {}", role);
        return role;
    }

    /**
     * Сохраняет роль в базе данных.
     * @param role Роль для сохранения.
     */
    @Override
    public void save(Role role) {
        logger.info("Сохранение роли: {}", role.getRoleName());
        roleRepository.save(role);
        logger.debug("Роль успешно сохранена: {}", role);
    }

    @Override
    public Role findByRoleName(String roleName) {
        logger.info("Поиск роли по имени: {}", roleName);
        Role role = roleRepository.findByRoleName(roleName);
        logger.debug("Результат поиска роли {}: {}", roleName, role != null ? role : "не найдена");
        return role;
    }
}
