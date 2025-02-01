document.addEventListener("DOMContentLoaded", () => {
    const tableBody = document.querySelector("tbody");

    function loadUserData() {
        fetch("http://localhost:8080/admin/infoByThisUser")
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
                console.error("Ошибка загрузки данных пользователя:", error);
            });
    }

    function renderUserData(user) {
        tableBody.innerHTML = ""; // Очищаем перед добавлением

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

    loadUserData(); // Загружаем данные при загрузке страницы
});
