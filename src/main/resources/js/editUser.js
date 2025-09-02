// Функция для отправки данных отредактированного пользователя на сервер
async function sendDataEditUser(user) {
    try {
        const response = await fetch(`/api/admin`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                id: user.id,
                name: user.name,
                lastname: user.lastname,
                age: user.age,
                email: user.email,
                password: user.password,
                role: user.role.map(r => ({id: r.id}))
            })
        });

        if (!response.ok) {
            throw new Error('Не удалось обновить пользователя');
        }
        console.log(`Пользователь ${user.email} успешно обновлен`);
        return await response.json();
    } catch (error) {
        console.error('Ошибка редактирования:', error);
        throw error;
    }
}

// Получение элемента модального окна для редактирования пользователя
const modalEdit = document.getElementById("editModal");

// Функция для заполнения модального окна данными пользователя
async function EditModalHandler() {
    try {
        await fillModal(modalEdit);
        console.log("Модальное окно редактирования заполнено");
    } catch (error) {
        console.error('Ошибка заполнения модального окна:', error);
        showAlert('Ошибка загрузки данных для редактирования', 'danger');
    }
}

// Добавление обработчика события на форму модального окна при отправке данных (submit)
modalEdit.addEventListener("submit", async function (event) {
    event.preventDefault();

    try {
        const rolesSelected = document.getElementById("rolesEdit");
        const roles = Array.from(rolesSelected.selectedOptions)
            .map(option => ({
                id: option.value === "ROLE_USER" ? 1 : 2,
                roleName: option.value
            }));

        const user = {
            id: document.getElementById("idEdit").value,
            name: document.getElementById("firstNameEdit").value,
            lastname: document.getElementById("lastNameEdit").value,
            age: document.getElementById("ageEdit").value,
            email: document.getElementById("emailEdit").value,
            password: document.getElementById("passwordEdit").value,
            role: roles
        };

        await sendDataEditUser(user);
        await fillTableOfAllUsers();
        $('#editModal').modal('hide');
        showAlert('Пользователь успешно обновлен', 'success');
    } catch (error) {
        console.error('Ошибка отправки формы редактирования:', error);
        showAlert(`Ошибка обновления пользователя: ${error.message}`, 'danger');
    }
});

// Вспомогательная функция для показа уведомлений
function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    `;

    const container = document.querySelector('.container-fluid .row .col-10');
    container.prepend(alertDiv);

    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}