document.addEventListener("DOMContentLoaded", function () {
    fetch('/admin/infoByThisUser')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user data');
            }
            return response.json();
        })
        .then(user => {
            document.getElementById('user-info').innerText = `${user.email} with roles: ADMIN USER`;
        })
        .catch(error => {
            console.error('Error fetching user info:', error);
            document.getElementById('user-info').innerText = 'Failed to load user info';
        });
});
