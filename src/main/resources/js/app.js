// Константы для ролей пользователя
const ROLE_USER = {id: 1, role: "ROLE_USER"}; // Роль "пользователь"
const ROLE_ADMIN = {id: 2, role: "ROLE_ADMIN"}; // Роль "администратор"

// Добавление обработчика события, который запускается, когда DOM полностью загружен
document.addEventListener('DOMContentLoaded', async function () {
    try {
        await Promise.all([
            showUserEmailOnNavbar(), // Показать email текущего пользователя в навбаре
            fillTableOfAllUsers(),   // Заполнить таблицу всех пользователей
            fillTableAboutCurrentUser(), // Заполнить таблицу текущего пользователя
            addNewUserForm(), // Обработчик для добавления нового пользователя
            DeleteModalHandler(), // Обработчик для удаления пользователя
            EditModalHandler() // Обработчик для редактирования пользователя
        ]);
        console.log("Все инициализационные функции выполнены");
    } catch (error) {
        console.error('Ошибка инициализации:', error);
        showAlert('Ошибка загрузки данных приложения', 'danger');
    }
});

// Функция для отображения email текущего пользователя в навбаре
async function showUserEmailOnNavbar() {
    try {
        // Получение элемента навбара, где будет отображаться email
        const currentUserEmailNavbar = document.getElementById("currentUserEmailNavbar");

        // Получение данных о текущем пользователе
        const currentUser = await dataAboutCurrentUser();

        // Заполнение HTML-контента элементом с email и ролями текущего пользователя
        currentUserEmailNavbar.innerHTML =
            `<strong>${currentUser.email}</strong>
             with roles: 
             ${currentUser.role.map(role => role.roleName).join(' ')}`;
        console.log(`Email пользователя ${currentUser.email} отображен в навбаре`);
    } catch (error) {
        console.error('Ошибка отображения email:', error);
        throw error;
    }
}