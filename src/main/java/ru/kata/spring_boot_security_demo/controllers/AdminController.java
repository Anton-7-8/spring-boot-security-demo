package ru.kata.spring_boot_security_demo.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring_boot_security_demo.models.Role;
import ru.kata.spring_boot_security_demo.models.User;
import ru.kata.spring_boot_security_demo.service.RoleService;
import ru.kata.spring_boot_security_demo.service.UserService;
import ru.kata.spring_boot_security_demo.util.PersonValidator;

import java.util.List;

/**
 * Контроллер для админских операций.
 * Обрабатывает запросы, связанные с управлением пользователями.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    // Логгер для отслеживания операций в контроллере.
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final PersonValidator personValidator;

    private final RoleService roleService;
    private final UserService userService;

    /**
     * Конструктор.
     * @param personValidator Валидатор для проверки данных пользователя.
     * @param roleService Сервис для работы с ролями.
     * @param userService Сервис для работы с пользователями.
     */
    @Autowired
    public AdminController(PersonValidator personValidator, RoleService roleService, UserService userService) {
        this.personValidator = personValidator;
        this.roleService = roleService;
        this.userService = userService;
    }
    /**
     * Отображает страницу со списком всех пользователей.
     * @param user Текущий аутентифицированный пользователь.
     * @param model Модель для передачи данных в представление.
     * @return Название шаблона для отображения (users).
     */

    @GetMapping("/users")
    public String showAllUsers(@AuthenticationPrincipal User user, Model model) {
        logger.info("Запрос списка пользователей от пользователя: {}", user.getEmail());
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("person", new User());
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        return "users";
    }
    /**
     * Добавляет нового пользователя.
     * @param user Объект пользователя из формы.
     * @param bindingResult Результаты валидации.
     * @return Перенаправление на страницу пользователей или возврат формы при ошибке.
     */
    @PostMapping("/addNewUser")
    public String addNewUser(@ModelAttribute("person") @Valid User user, BindingResult bindingResult) {
        logger.info("Попытка добавления нового пользователя: {}", user.getEmail());
        personValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.warn("Ошибки валидации при добавлении пользователя: {}", bindingResult.getAllErrors());
            return "users";
        }
        userService.addNewUser(user);
        logger.info("Пользователь успешно добавлен: {}", user.getEmail());
        return "redirect:/admin/users";
    }
    /**
     * Редактирует существующего пользователя.
     * @param user Объект пользователя с обновленными данными.
     * @return Перенаправление на страницу пользователей.
     */
    @PostMapping("/edit")
    public String editUser(@ModelAttribute("showUser") @Valid User user) {
        logger.info("Редактирование пользователя с ID: {}", user.getId());
        userService.edit(user);
        logger.info("Пользователь успешно отредактирован: {}", user.getEmail());
        return "redirect: /admin/users";
    }
    /**
     * Удаляет пользователя по ID.
     * @param user Объект пользователя с ID для удаления.
     * @return Перенаправление на страницу пользователей.
     */
    @PostMapping("/delete")
    public String deleteUserId (@ModelAttribute("showUser") User user) {
        logger.info("Удаление пользователя с ID: {}", user.getId());
        userService.deleteUser(user.getId());
        logger.info("Пользователь успешно удален: {}", user.getId());
        return "redirect:/admin/users";
    }
}
