document.addEventListener("DOMContentLoaded", function () {
    // Close modal event listeners
    document.querySelectorAll(".close-btn, #closeModal, #closeDeleteModal").forEach(btn => {
        btn.addEventListener("click", function () {
            document.getElementById("editModal").style.display = "none";
            document.getElementById("deleteModal").style.display = "none";
        });
    });

    // Function to open edit modal
    function openEditModal(user) {
        document.getElementById("userId").value = user.id;
        document.getElementById("firstName").value = user.firstName;
        document.getElementById("lastName").value = user.lastName;
        document.getElementById("age").value = user.age;
        document.getElementById("email").value = user.email;
        document.getElementById("role").value = user.role;
        document.getElementById("editModal").style.display = "block";
    }

    // Function to open delete modal
    function openDeleteModal(userId) {
        document.getElementById("deleteUserId").value = userId;
        document.getElementById("deleteModal").style.display = "block";
    }

    // Example event listener for edit buttons
    document.querySelectorAll(".edit-btn").forEach(button => {
        button.addEventListener("click", function () {
            const userId = this.dataset.userId;
            // Fetch user details from server or use local data
            openEditModal({ id: userId, firstName: "John", lastName: "Doe", age: 30, email: "john@example.com", role: "ROLE_USER" });
        });
    });
});
