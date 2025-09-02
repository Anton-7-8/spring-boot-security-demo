// Функция для получения данных пользователя по его ID
async function getUserDataById(userId) {
    try {
        const response = await fetch(`/api/admin/${userId}`); // Отправка запроса на сервер
        if (!response.ok) {
            throw new Error(`Ошибка получения данных пользователя: ${response.statusText}`);
        }
        const user = await response.json();
        console.log(`Данные пользователя с ID ${userId} получены`);
        return user;
    } catch (error) {
        console.error('Ошибка получения данных пользователя:', error);
        throw error;
    }
}

// Функция для заполнения модального окна
async function fillModal(modal) {
    // Добавление обработчика события при показе модального окна
    $(modal).on("show.bs.modal", async function (event) {
        try {
            const userId = $(event.relatedTarget).data('user-id'); // Получение ID пользователя
            const user = await getUserDataById(userId); // Получение данных пользователя
            const modalBody = $(this).find(".modal-body"); // Поиск тела модального окна

            // Поиск элементов формы
            const idInput = modalBody.find("input[data-user-id='id']");
            const nameInput = modalBody.find("input[data-user-id='name']");
            const lastNameInput = modalBody.find("input[data-user-id='lastName']");
            const ageInput = modalBody.find("input[data-user-id='age']");
            const emailInput = modalBody.find("input[data-user-id='email']");
            const passwordInput = modalBody.find("input[data-user-id='password']");

            // Заполнение поля пароля, если оно существует
            if (passwordInput.length) {
                passwordInput.val(user.password);
            }

            // Заполнение полей формы
            idInput.val(user.id);
            nameInput.val(user.name);
            lastNameInput.val(user.lastname);
            ageInput.val(user.age);
            emailInput.val(user.email);

            let rolesSelect;
            const rolesSelectDelete = modalBody.find("select[data-user-id='rolesDelete']");
            const rolesSelectEdit = modalBody.find("select[data-user-id='rolesEdit']");
            let userRolesHTML = "";

            if (rolesSelectDelete.length) {
                rolesSelect = rolesSelectDelete;
                user.role.forEach(role => {
                    userRolesHTML += `<option value="${role.id}">${role.roleName}</option>`;
                });
            } else if (rolesSelectEdit.length) {
                rolesSelect = rolesSelectEdit;
                userRolesHTML += `
                    <option value="ROLE_USER">USER</option>
                    <option value="ROLE_ADMIN">ADMIN</option>`;
                user.role.forEach(role => {
                    rolesSelect.find(`option[value="${role.roleName}"]`).prop('selected', true);
                });
            }

            rolesSelect.html(userRolesHTML);
            console.log(`Модальное окно заполнено для пользователя с ID ${userId}`);
        } catch (error) {
            console.error('Ошибка заполнения модального окна:', error);
            showAlert('Ошибка загрузки данных модального окна', 'danger');
        }
    });
}