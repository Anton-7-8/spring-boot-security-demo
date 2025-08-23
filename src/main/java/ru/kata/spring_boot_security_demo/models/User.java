package ru.kata.spring_boot_security_demo.models;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Модель пользователя.
 * Реализует UserDetails для интеграции с Spring Security.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
public class User implements UserDetails {

    // Логгер для отслеживания операций с пользователем.
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String name;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "age")
    @Min(value = 0, message = "Возраст должен быть больше 0")
    private int age;

    @Column(name = "email", unique = true, length = 255)
    @Email
    @NotEmpty(message = "Email не должен быть пустым")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> role = new HashSet<>();

    /**
     * Возвращает роли пользователя для Spring Security.
     * @return Коллекция объектов GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = role.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
        logger.debug("Получение authorities для пользователя {}: {}", email, authorities);
        return authorities;
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта.
     * @return true (аккаунт всегда активен).
     */
    @Override
    public boolean isAccountNonExpired() {
        logger.debug("Проверка isAccountNonExpired для пользователя {}: true", email);
        return true;
    }

    /**
     * Возвращает имя пользователя (email).
     * @return Email пользователя.
     */
    @Override
    public String getUsername() {
        logger.debug("Получение имени пользователя: {}", email);
        return email;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт.
     * @return true (аккаунт всегда не заблокирован).
     */
    @Override
    public boolean isAccountNonLocked() {
        logger.debug("Проверка isAccountNonLocked для пользователя {}: true", email);
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных.
     * @return true (учетные данные всегда активны).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        logger.debug("Проверка isCredentialsNonExpired для пользователя {}: true", email);
        return true;
    }

    /**
     * Проверяет, включен ли аккаунт.
     * @return true (аккаунт всегда включен).
     */
    @Override
    public boolean isEnabled() {
        logger.debug("Проверка isEnabled для пользователя {}: true", email);
        return true;
    }
}