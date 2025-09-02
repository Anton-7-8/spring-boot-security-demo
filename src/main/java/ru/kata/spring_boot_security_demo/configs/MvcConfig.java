package ru.kata.spring_boot_security_demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Класс конфигурации для настройки MVC в Spring.
 * Реализует интерфейс WebMvcConfigurer для настройки обработчиков ресурсов и CORS.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    // Логгер для класса, чтобы отслеживать события в методах конфигурации (если нужно).
    private static final Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    /**
     * Метод добавляет обработчики статических ресурсов.
     * Настраивает путь для JavaScript файлов из classpath.
     */
    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        logger.info("Настройка обработчиков ресурсов для /js/**");
        registry.addResourceHandler("/js/**")
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    /**
     * Метод настраивает CORS (Cross-Origin Resource Sharing).
     * Разрешает запросы с любых источников для всех путей.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
