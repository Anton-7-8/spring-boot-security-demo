// Функция для удаления данных пользователя по его ID
async function deleteUserData(userId) {
    try {
        const response = await fetch(`/api/admin/${userId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Не удалось удалить пользователя');
        }
        console.log(`Пользователь с ID ${userId} успешно удален`);
        return await response.json();
    } catch (error) {
        console.error('Ошибка удаления:', error);
        throw error;
    }
}

// Получение элемента модального окна для удаления пользователя
const modalDelete = document.getElementById("deleteModal");

// Функция для заполнения модального окна данными пользователя
async function DeleteModalHandler() {
    try {
        await fillModal(modalDelete);
        console.log("Модальное окно удаления заполнено");
    } catch (error) {
        console.error('Ошибка заполнения модального окна:', error);
        showAlert('Ошибка загрузки данных для удаления', 'danger');
    }
}

// Обработка события отправки формы удаления
const formDelete = document.getElementById("modalBodyDelete");
if (formDelete) {
    formDelete.addEventListener("submit", async function (event) {
        event.preventDefault();
        try {
            const userId = event.target.querySelector("#idDelete").value;
            await deleteUserData(userId);
            await fillTableOfAllUsers();
            $('#deleteModal').modal('hide');
            showAlert('Пользователь успешно удален', 'success');
        } catch (error) {
            console.error('Ошибка отправки формы удаления:', error);
            showAlert(`Ошибка удаления пользователя: ${error.message}`, 'danger');
        }
    });
}

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