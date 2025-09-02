package ru.kata.spring_boot_security_demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.kata.spring_boot_security_demo.service.UserDetailsServiceImp;

/**
 * Класс конфигурации безопасности Spring Security.
 * Настраивает правила доступа, аутентификацию и шифрование паролей.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {
    // Логгер для отслеживания конфигурации безопасности.
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsServiceImp userDetailsServiceImp;

    /**
     * Конструктор для инъекции зависимостей.
     *
     * @param successUserHandler    Обработчик успешной аутентификации.
     * @param userDetailsServiceImp Сервис для загрузки данных пользователя.
     */
    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailsServiceImp userDetailsServiceImp) {
        logger.info("Инициализация WebSecurityConfig");
        this.successUserHandler = successUserHandler;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }
    /**
     * Бин для цепочки фильтров безопасности.
     * Настраивает правила авторизации, отключение CSRF, форму логина и логаут.
     *
     * @param http Объект HttpSecurity для настройки.
     * @return SecurityFilterChain Цепочка фильтров.
     * @throws Exception Если возникает ошибка конфигурации.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Настройка SecurityFilterChain");
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Объединенное правило для всех операций /api/admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Только админы могут обращаться к /admin/**
                        .requestMatchers("/api/user/current").hasRole("USER") // Доступ для пользователей к текущему профилю
                        .requestMatchers("/api/user").hasRole("USER") // Пользователи могут обращаться к /api/user
                        .requestMatchers("/user").hasRole("USER") // Пользователи могут обращаться к /user
                        .requestMatchers("/login", "/js/**", "/css/**").permitAll() // Разрешить доступ к статическим ресурсам и логину
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации

                )
                .csrf(csrf -> csrf.disable()) // Отключение CSRF для упрощения API (небезопасно для продакшена)
                .formLogin(form -> form
                        .loginPage("/login") // Страница логина
                        .loginProcessingUrl("/process_login") // URL для обработки логина
                        .failureUrl("/login?error") // URL при ошибке логина
                        .successHandler(successUserHandler) // Обработчик успешного логина
                        .permitAll() // Доступ к форме логина для всех
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .logoutSuccessUrl("/login") // URL после успешного выхода
                        .permitAll() // Доступ к логауту для всех
                )
                .userDetailsService(userDetailsServiceImp); // Сервис для загрузки данных пользователя

        return http.build();
    }

    /**
     * Бин для шифровщика паролей.
     * Использует BCrypt для безопасного хэширования.
     * @return PasswordEncoder Шифровщик паролей.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        logger.info("Создание PasswordEncoder");
        return new BCryptPasswordEncoder();
    }
}
