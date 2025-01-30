document.addEventListener("DOMContentLoaded", () => {
    const tableBody = document.querySelector("tbody");

    // Функция для загрузки данных пользователя
    function loadUserData() {
        fetch("http://localhost:8080/user")
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(userData => {
                renderUserData(userData);
            })
            .catch(error => {
                console.error("Error fetching user data:", error);
            });
    }

    // Функция для отображения данных в таблице
    function renderUserData(user) {
        // Очищаем тело таблицы перед добавлением данных
        tableBody.innerHTML = "";

        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => role.roleName).join(", ")}</td>
        `;
        tableBody.appendChild(row);
    }

    // Загружаем данные при загрузке страницы
    loadUserData();
});
