package ru.kata.spring_boot_security_demo.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring_boot_security_demo.models.Role;
import ru.kata.spring_boot_security_demo.models.User;
import ru.kata.spring_boot_security_demo.repositories.RoleRepository;
import ru.kata.spring_boot_security_demo.repositories.UserRepository;
import ru.kata.spring_boot_security_demo.util.UserNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

    // Логгер для отслеживания операций в сервисе.
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Конструктор для инъекции зависимостей.
     * @param userRepository Репозиторий пользователей.
     * @param passwordEncoder Шифровщик паролей.
     * @param roleRepository Репозиторий ролей.
     */
    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        logger.info("Инициализация UserServiceImp");
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    /**
     * Возвращает список всех пользователей с ролями.
     * @return Список пользователей.
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.info("Запрос списка всех пользователей");
        List<User> users = userRepository.findAllWithRoles();
        logger.debug("Найдено пользователей: {}", users.size());
        return users;
    }

    /**
     * Находит пользователя по ID.
     * @param id Идентификатор пользователя.
     * @return Пользователь.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        logger.info("Поиск пользователя с ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        User foundUser = user.orElseThrow(()-> {
            logger.error("Пользователь с ID {} не найден", id);
            return new UserNotFoundException();
        });
        logger.debug("Пользователь найден: {}", foundUser.getEmail());
        return foundUser;
    }

    /**
     * Находит пользователя по ID с подгруженными ролями.
     * @param id Идентификатор пользователя.
     * @return Пользователь с ролями.
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserWithRoles(Long id) {
        logger.info("Поиск пользователя с ролями по ID: {}", id);
        User user = userRepository.getUserWithRoles(id);
        logger.debug("Пользователь с ролями найден: {}", user.getEmail());
        return user;
    }
    /**
     * Удаляет пользователя по ID.
     * @param id Идентификатор пользователя.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Удаление пользователя с ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            logger.error("Пользователь с ID {} не найден для удаления", id);
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
        logger.debug("Пользователь с ID {} успешно удален", id);
    }

    /**
     * Добавляет нового пользователя.
     * @param user Пользователь для добавления.
     */
    @Override
    @Transactional
    public void addNewUser(User user) {
        logger.info("Добавление нового пользователя: {}", user.getEmail());
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRole()) {
            Optional<Role> foundRole = roleRepository.findById(role.getId());
            if (foundRole.isPresent()) {
                roles.add(foundRole.get());
                logger.debug("Добавлена роль {} для пользователя {}", foundRole.get().getRoleName(), user.getEmail());
            } else {
                logger.error("Роль с ID {} не найдена", role.getId());
                throw new RuntimeException("Role not found with id: " + role.getId());
            }
        }
        user.setRole(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.debug("Пользователь успешно добавлен: {}", user.getEmail());
    }

    /**
     * Редактирует существующего пользователя.
     * @param user Пользователь с обновленными данными.
     * @throws EntityNotFoundException Если пользователь не найден.
     */
    @Override
    @Transactional
    public void edit(User user) {
        logger.info("Редактирование пользователя: {}", user.getEmail());
        if (user == null) {
            logger.error("Передан null пользователь для редактирования");
            throw new EntityNotFoundException("User not found");
        }
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            logger.error("Пользователь с ID {} не найден для редактирования", user.getId());
            throw new EntityNotFoundException("User not found");
        }
        // Проверяет, если пароль совпадает с текущим - сохраняет, если нет - шифрует
        String currentPassword = existingUser.get().getPassword();
        if (!user.getPassword().equals(currentPassword)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.debug("Пароль пользователя {} обновлен", user.getEmail());
        }
        userRepository.save(user);
        logger.debug("Пользователь успешно отредактирован: {}", user.getEmail());
    }
}
