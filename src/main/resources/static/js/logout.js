function logout() {
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Deseas cerrar sesión?',
        icon: 'warning',
		iconColor: '#ffcc00',
        showCancelButton: true,
        confirmButtonText: 'Sí, cerrar sesión',
        cancelButtonText: 'Cancelar',
        customClass: {
            confirmButton: 'btn-confirm-logout', 
            cancelButton: 'btn-cancel-logout'    
        }
    }).then((result) => {
        if (result.isConfirmed) {     
            fetch('/logout', {
                method: 'POST',
                credentials: 'same-origin'  
            }).then(response => {
                window.location.href = "/";  
            }).catch(error => {
                console.error('Error al cerrar sesión:', error);  
            });
        }
    });
}
