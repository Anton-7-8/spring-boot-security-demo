// Добавляем обработчик события, который срабатывает после полной загрузки DOM
document.addEventListener('DOMContentLoaded', async () => {
    try {
        // Используем Promise.all для параллельного выполнения асинхронных функций
        // Это позволяет одновременно загрузить данные для навбара и таблицы
        await Promise.all([
            showUserEmailOnNavbar(), // Отображение email и ролей в навбаре
            fillTableAboutUser()     // Заполнение таблицы данными текущего пользователя
        ]);
        // Логируем успешное завершение инициализации страницы
        console.log("Инициализация страницы пользователя завершена");
    } catch (error) {
        // Обрабатываем ошибки, которые могут возникнуть при инициализации
        console.error('Ошибка инициализации:', error);
        // Показываем уведомление пользователю об ошибке
        showErrorAlert('Не удалось загрузить данные пользователя');
    }
});

// Асинхронная функция для получения данных текущего пользователя с сервера
async function fetchCurrentUser() {
    try {
        // Отправляем GET-запрос к API для получения данных текущего пользователя
        const response = await fetch("/api/user/current", {
            headers: {
                'Accept': 'application/json' // Указываем, что ожидаем JSON в ответе
            }
        });
        // Проверяем, успешен ли запрос (статус 200-299)
        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }
        // Парсим ответ в формате JSON
        const user = await response.json();
        // Логируем успешное получение данных пользователя
        console.log(`Данные пользователя ${user.email} получены`);
        return user; // Возвращаем объект пользователя
    } catch (error) {
        // Обрабатываем ошибки, возникшие при запросе данных
        console.error('Ошибка получения данных пользователя:', error);
        throw error; // Пробрасываем ошибку дальше для обработки в вызывающей функции
    }
}

// Асинхронная функция для отображения email и ролей текущего пользователя в навбаре
async function showUserEmailOnNavbar() {
    try {
        // Получаем данные текущего пользователя
        const user = await fetchCurrentUser();
        // Находим элемент навбара по ID
        const navbar = document.getElementById("currentUserEmailNavbar");
        // Формируем HTML для отображения email и ролей пользователя
        // Роли преобразуем в строку, разделяя запятыми
        navbar.innerHTML = `
            <strong>${user.email}</strong> with roles: 
            ${user.role.map(r => r.roleName).join(', ')}
        `;
        // Логируем успешное отображение email в навбаре
        console.log(`Email пользователя ${user.email} отображен в навбаре`);
    } catch (error) {
        // Обрабатываем ошибки, возникшие при отображении данных
        console.error('Ошибка отображения email:', error);
        // Показываем уведомление пользователю об ошибке
        showErrorAlert('Ошибка загрузки email пользователя');
    }
}

// Асинхронная функция для заполнения таблицы данными текущего пользователя
async function fillTableAboutUser() {
    try {
        // Получаем данные текущего пользователя
        const user = await fetchCurrentUser();
        // Находим тело таблицы по ID
        const tableBody = document.getElementById("currentUserTable");

        // Формируем HTML для строки таблицы с данными пользователя
        // Включаем ID, имя, фамилию, возраст, email и роли (разделенные запятыми)
        tableBody.innerHTML = `
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.lastname}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.role.map(r => r.roleName).join(', ')}</td>
            </tr>
        `;
        // Логируем успешное заполнение таблицы
        console.log(`Таблица пользователя ${user.email} заполнена`);
    } catch (error) {
        // Обрабатываем ошибки, возникшие при заполнении таблицы
        console.error('Ошибка заполнения таблицы:', error);
        // Показываем уведомление пользователю об ошибке
        showErrorAlert('Ошибка загрузки данных пользователя');
    }
}

// Функция для отображения уведомления об ошибке
function showErrorAlert(message) {
    // Формируем HTML для уведомления с классами Bootstrap
    // Уведомление автоматически исчезает через 5 секунд
    const alert = `
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    `;
    // Добавляем уведомление в начало контейнера .container-fluid
    document.querySelector('.container-fluid').insertAdjacentHTML('afterbegin', alert);
}