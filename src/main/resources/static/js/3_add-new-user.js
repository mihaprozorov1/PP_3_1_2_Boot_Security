document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(form);
        const userData = {
            username: formData.get("first-name"),
            lastName: formData.get("last-name"),
            age: Number(formData.get("age")),
            email: formData.get("email"),
            password: formData.get("password"),
            roles: [formData.get("role")] // Передача роли как массива
        };

        try {
            const response = await fetch("http://localhost:8080/admin/3_add-new-user", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(userData),
                credentials: "include" // Передача cookies (если нужна аутентификация)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            const result = await response.json();
            alert("User created successfully! ID: " + result.id);
            form.reset(); // Очистка формы после успешного создания пользователя
        } catch (error) {
            console.error("Ошибка при создании пользователя:", error);
            alert("Error creating user: " + error.message);
        }
    });
});
