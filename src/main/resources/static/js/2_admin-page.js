document.addEventListener("DOMContentLoaded", () => {
    const usersTableBody = document.querySelector("tbody");
    const editModal = document.getElementById("editModal");
    const deleteModal = document.getElementById("deleteModal");

    function fetchUsers() {
        fetch("/admin")
            .then(response => response.json())
            .then(users => {
                usersTableBody.innerHTML = ""; // Очищаем таблицу перед обновлением
                users.forEach(user => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${user.roles.map(role => role.roleName).join(", ")}</td>
                        <td><button class="edit-btn" data-id="${user.id}">Edit</button></td>
                        <td><button class="delete-btn" data-id="${user.id}">Delete</button></td>
                    `;
                    usersTableBody.appendChild(row);
                });
                attachEventListeners();
            })
            .catch(error => console.error("Ошибка загрузки пользователей:", error));
    }

    function attachEventListeners() {
        document.querySelectorAll(".edit-btn").forEach(button => {
            button.addEventListener("click", (event) => {
                const userId = event.target.dataset.id;
                fetch(`/admin/${userId}`)
                    .then(response => response.json())
                    .then(user => {
                        document.getElementById("userId").value = user.id;
                        document.getElementById("firstName").value = user.username;
                        document.getElementById("lastName").value = user.lastName;
                        document.getElementById("age").value = user.age;
                        document.getElementById("email").value = user.email;
                        document.getElementById("role").value = user.roles[0].roleName;
                        editModal.style.display = "block";
                    });
            });
        });

        document.querySelectorAll(".delete-btn").forEach(button => {
            button.addEventListener("click", (event) => {
                const userId = event.target.dataset.id;
                document.getElementById("deleteUserId").value = userId;
                deleteModal.style.display = "block";
            });
        });
    }

    document.getElementById("editForm").addEventListener("submit", (event) => {
        event.preventDefault();
        const userId = document.getElementById("userId").value;
        const updatedUser = {
            id: userId,
            username: document.getElementById("firstName").value,
            lastName: document.getElementById("lastName").value,
            age: document.getElementById("age").value,
            email: document.getElementById("email").value
        };
        fetch(`/admin/?role=${document.getElementById("role").value}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedUser)
        }).then(() => {
            editModal.style.display = "none";
            fetchUsers();
        });
    });

    document.getElementById("deleteForm").addEventListener("submit", (event) => {
        event.preventDefault();
        const userId = document.getElementById("deleteUserId").value;
        fetch(`/admin/${userId}`, { method: "DELETE" })
            .then(() => {
                deleteModal.style.display = "none";
                fetchUsers();
            });
    });

    document.getElementById("closeModal").addEventListener("click", () => editModal.style.display = "none");
    document.getElementById("closeDeleteModal").addEventListener("click", () => deleteModal.style.display = "none");

    fetchUsers();
});
