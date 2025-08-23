package ru.kata.spring_boot_security_demo.models;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Модель роли пользователя.
 * Реализует GrantedAuthority для интеграции с Spring Security.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {

    // Логгер для отслеживания операций с ролями.
    private static final Logger logger = LoggerFactory.getLogger(Role.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    /**
     * Возвращает имя роли для Spring Security.
     * @return Имя роли как строка авторизации.
     */
    @Override
    public String getAuthority() {
        logger.debug("Получение authority для роли: {}", roleName);
        return roleName;
    }
}