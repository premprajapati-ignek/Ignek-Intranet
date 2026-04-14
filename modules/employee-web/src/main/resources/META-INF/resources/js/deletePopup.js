function openDeletePopup(url) {
    deleteUrl = url;
    document.getElementById("deleteModal").style.display = "block";
}

function closePopup() {
    document.getElementById("deleteModal").style.display = "none";
}

function confirmDelete() {
    window.location.href = deleteUrl;
}