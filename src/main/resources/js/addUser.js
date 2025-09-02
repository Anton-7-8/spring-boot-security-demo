// Асинхронная функция для создания нового пользователя с использованием POST-запроса к API
async function createNewUser(user) {
    try {
        const response = await fetch("/api/admin", {
            method: 'POST', // Метод HTTP-запроса - POST
            headers: {'Content-Type': 'application/json'}, // Устанавливаем заголовок Content-Type для отправки JSON
            body: JSON.stringify(user) // Тело запроса, преобразованное в JSON
        });
        if (!response.ok) {
            throw new Error(`Ошибка создания пользователя: ${response.statusText}`);
        }
        console.log(`Пользователь ${user.email} успешно создан`);
    } catch (error) {
        console.error('Ошибка при создании пользователя:', error);
        throw error;
    }
}

// Асинхронная функция для добавления обработчика формы нового пользователя
async function addNewUserForm() {
    // Получаем элемент формы нового пользователя по его ID
    const newUserForm = document.getElementById("newUser");

    // Добавляем обработчик события 'submit' на форму
    newUserForm.addEventListener('submit', async function (event) {
        event.preventDefault(); // Предотвращаем стандартное поведение формы (перезагрузку страницы)

        // Получаем значения полей формы и обрезаем пробелы
        const name = newUserForm.querySelector("#name").value.trim();
        const lastname = newUserForm.querySelector("#lastName").value.trim();
        const age = newUserForm.querySelector("#age").value.trim();
        const email = newUserForm.querySelector("#email").value.trim();
        const password = newUserForm.querySelector("#password").value.trim();

        // Получаем элемент для выбора ролей по его ID
        const rolesSelected = document.getElementById("roles");

        // Создаем массив для ролей
        let role = [];
        // Перебираем выбранные роли и добавляем их в массив roles
        for (let option of rolesSelected.selectedOptions) {
            if (option.value === ROLE_USER.role) { // Если значение роли равно "ROLE_USER"
                role.push(ROLE_USER); // Добавляем роль пользователя в массив roles
            } else if (option.value === ROLE_ADMIN.role) { // Если значение роли равно "ROLE_ADMIN"
                role.push(ROLE_ADMIN); // Добавляем роль администратора в массив roles
            }
        }

        // Создаем объект нового пользователя с собранными данными
        const newUserData = {
            name: name,
            lastname: lastname,
            age: age,
            email: email,
            password: password,
            role: role
        };

        try {
            // Отправляем данные нового пользователя на сервер для создания
            await createNewUser(newUserData);
            // Сбрасываем форму после успешного создания пользователя
            newUserForm.reset();
            // Кликаем на элемент, который отображает таблицу пользователей (переключаем вкладку)
            document.querySelector('a#home-tab').click();
            // Обновляем таблицу всех пользователей после добавления нового пользователя
            await fillTableOfAllUsers();
            showAlert('Пользователь успешно добавлен', 'success');
        } catch (error) {
            showAlert(`Ошибка добавления пользователя: ${error.message}`, 'danger');
        }
    });
}