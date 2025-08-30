package ru.kata.spring_boot_security_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring_boot_security_demo.models.User;
import ru.kata.spring_boot_security_demo.repositories.UserRepository;

import java.util.Optional;

/**
 * Сервис для загрузки данных пользователя для Spring Security.
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImp implements UserDetailsService {

    // Логгер для отслеживания операций в сервисе.
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImp.class);

    private final UserRepository userRepository;

    /**
     * Конструктор для инъекции зависимостей.
     * @param userRepository Репозиторий пользователей.
     */
    @Autowired
    public UserDetailsServiceImp(UserRepository userRepository) {
        logger.info("Инициализация UserDetailsServiceImp");
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по email для аутентификации.
     * @param email Email пользователя.
     * @return UserDetails Объект пользователя.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Поиск пользователя по email: {}", email);
        Optional<User> person = userRepository.findByEmail(email);
        if (person.isEmpty()) {
            logger.error("Пользователь с email {} не найден", email);
            throw new UsernameNotFoundException("User not found");
        }
        logger.debug("Пользователь найден: {}", person.get().getEmail());
        return person.get();
    }
}
