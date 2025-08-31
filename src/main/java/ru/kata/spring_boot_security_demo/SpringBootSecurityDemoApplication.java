package ru.kata.spring_boot_security_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс приложения Spring Boot.
 */
@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	// Логгер для отслеживания запуска приложения.
	private static final Logger logger = LoggerFactory.getLogger(SpringBootSecurityDemoApplication.class);

	/**
	 * Точка входа приложения.
	 * @param args Аргументы командной строки.
	 */
	public static void main(String[] args) {
		logger.info("Запуск приложения SpringBootSecurityDemoApplication");
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
		logger.info("Приложение успешно запущено");
	}
}
