document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("addUserForm");

    form.addEventListener("submit", async function (event) {
        event.preventDefault(); // Останавливаем стандартную отправку формы

        // Формируем JSON объект пользователя
        const user = {
            username: document.getElementById("first-name").value,
            lastName: document.getElementById("last-name").value,
            age: document.getElementById("age").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        const role = document.getElementById("role").value;
        const url = `/admin/?role=${encodeURIComponent(role)}`;

        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            });

            if (response.ok) {
                alert("Пользователь успешно добавлен!");
                form.reset();
            } else {
                const errorText = await response.text();
                alert("Ошибка: " + errorText);
            }
        } catch (error) {
            console.error("Ошибка при отправке запроса:", error);
            alert("Ошибка соединения с сервером!");
        }
    });
});