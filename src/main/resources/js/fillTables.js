// Функция для получения данных обо всех пользователях
async function dataAboutAllUsers() {
    try {
        const response = await fetch("/api/admin");
        if (!response.ok) {
            throw new Error(`Ошибка получения списка пользователей: ${response.statusText}`);
        }
        const users = await response.json();
        console.log(`Получено ${users.length} пользователей`);
        return users;
    } catch (error) {
        console.error('Ошибка получения данных пользователей:', error);
        throw error;
    }
}

// Функция для получения данных о текущем пользователе
async function dataAboutCurrentUser() {
    try {
        const response = await fetch("/api/user/current");
        if (!response.ok) {
            throw new Error(`Ошибка получения данных текущего пользователя: ${response.statusText}`);
        }
        const user = await response.json();
        console.log(`Данные текущего пользователя ${user.email} получены`);
        return user;
    } catch (error) {
        console.error('Ошибка получения данных текущего пользователя:', error);
        throw error;
    }
}

// Функция для заполнения таблицы всех пользователей в админ-панели
async function fillTableOfAllUsers() {
    try {
        const usersTable = document.getElementById("usersTable");
        const users = await dataAboutAllUsers();

        let usersTableHTML = "";
        for (let user of users) {
            usersTableHTML +=
                `<tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.lastname}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.role.map(role => role.roleName).join(' ')}</td>
                    <td>
                        <button class="btn btn-info btn-sm text-white"
                                data-toggle="modal"
                                data-target="#editModal"
                                data-user-id="${user.id}">
                            Edit</button>
                    </td>
                    <td>
                        <button class="btn btn-danger btn-sm btn-delete"
                                data-toggle="modal"
                                data-target="#deleteModal"
                                data-user-id="${user.id}">                     
                            Delete</button>
                    </td>
                </tr>`;
        }
        usersTable.innerHTML = usersTableHTML;
        console.log("Таблица пользователей заполнена");
    } catch (error) {
        console.error('Ошибка заполнения таблицы пользователей:', error);
        showAlert('Ошибка загрузки списка пользователей', 'danger');
    }
}

// Функция для заполнения таблицы текущего пользователя
async function fillTableAboutCurrentUser() {
    try {
        const currentUserTable = document.getElementById("currentUserTable");
        const currentUser = await dataAboutCurrentUser();

        let currentUserTableHTML = "";
        currentUserTableHTML +=
            `<tr>
                <td>${currentUser.id}</td>
                <td>${currentUser.name}</td>
                <td>${currentUser.lastname}</td>
                <td>${currentUser.age}</td>
                <td>${currentUser.email}</td>
                <td>${currentUser.role.map(role => role.roleName).join(' ')}</td>
            </tr>`;
        currentUserTable.innerHTML = currentUserTableHTML;
        console.log(`Таблица текущего пользователя ${currentUser.email} заполнена`);
    } catch (error) {
        console.error('Ошибка заполнения таблицы текущего пользователя:', error);
        showAlert('Ошибка загрузки данных текущего пользователя', 'danger');
    }
}