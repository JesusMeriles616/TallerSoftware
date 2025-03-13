<template>
  <div class="login-container">
    <h2>LISTA DE USUARIOS</h2>

    <!-- Campo de búsqueda -->
    <div class="search-container">
      <input 
        type="text" 
        v-model="searchQuery" 
        placeholder="Buscar por nombre o email..." 
        @input="searchUsers" 
      />
    </div>

    <!-- Tabla para mostrar usuarios -->
    <div class="table-container">
      <table v-if="filteredUsers.length > 0">
  <thead>
    <tr>
      <th>ID</th>
      <th>Nombre</th>
      <th>Email</th>
      <th>Acciones</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="user in filteredUsers" :key="user.id">
      <td>{{ user.id }}</td>
      <td>{{ user.nombre }}</td>
      <td>{{ user.email }}</td>
      <td><button @click="openEditModal(user)" style="margin-right: 10px;">Actualizar</button>
<button @click="confirmDelete(user.id)">Eliminar</button>

      </td>
    </tr>
  </tbody>
</table>
<p v-else>No hay usuarios registrados.</p>
    </div>

   

    <!-- Modal para Editar Usuario -->
    <div v-if="showEditModal" class="modal">
      <div class="modal-content">
        <h3>Editar Usuario</h3>
        <div class="modal-form-group">
          <label>Nombre:</label>
          <input type="text" v-model="selectedUser.nombre" placeholder="Nombre del usuario" />
        </div>
        <div class="modal-form-group">
          <label>Email:</label>
          <input type="email" v-model="selectedUser.email" placeholder="Correo del usuario" />
        </div>
        <div class="modal-buttons">
          <button @click="saveChanges">Guardar</button>
          <button @click="closeEditModal">Cancelar</button>
        </div>
      </div>
    </div>

    <!-- Modal de Confirmación para Eliminar -->
    <div v-if="showDeleteModal" class="modal">
      <div class="modal-content">
        <h3>¿Estás seguro de que deseas eliminar este usuario?</h3>
        <div class="modal-buttons">
          <button @click="deleteUser">Sí, eliminar</button>
          <button @click="showDeleteModal = false">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserService from '../service/UserService';

export default {
  name: 'ListaForm',
  data() {
    return {
      users: [],
      searchQuery: '',
      filteredUsers: [],
      showEditModal: false,
      showDeleteModal: false,
      selectedUser: {},
      deleteUserId: null,
    };
  },
  created() {
    this.userService = new UserService();
    this.fetchUsers();  // Cargar usuarios al inicializar
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await this.userService.getAll();
        this.users = response.data;
        this.filteredUsers = this.users;
      } catch (error) {
        console.error("Error al obtener usuarios:", error);
      }
    },
    async searchUsers() {
      if (this.searchQuery) {
        this.filteredUsers = this.users.filter(user =>
          user.nombre.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          user.email.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      } else {
        this.filteredUsers = this.users;
      }
    },
    openEditModal(user) {
      this.selectedUser = { ...user };
      this.showEditModal = true;
    },
    async saveChanges() {
      try {
        await this.userService.updateUser(this.selectedUser.id, this.selectedUser);
        this.showEditModal = false;
        this.fetchUsers();  // Recargar usuarios después de la actualización
      } catch (error) {
        console.error("Error al actualizar usuario:", error);
      }
    },
    closeEditModal() {
      this.showEditModal = false;
    },
    confirmDelete(id) {
      this.deleteUserId = id;
      this.showDeleteModal = true;
    },
    async deleteUser() {
      try {
        await this.userService.deleteUser(this.deleteUserId);
        this.showDeleteModal = false;
        this.fetchUsers();  // Recargar usuarios después de eliminar
      } catch (error) {
        console.error("Error al eliminar usuario:", error);
      }
    },
  },
};
</script>

<style scoped>
/* Contenedor principal */
.login-container {
  width: 90%;
  margin: auto;
  text-align: center;
  padding: 20px;
}

/* Tabla */
.table-container {
  overflow-x: auto;
  margin-top: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 12px;
  word-wrap: break-word;
  text-align: center;
}

th {
  background-color: #f2f2f2;
  font-weight: bold;
}

/* Botones */
button {
  padding: 8px 16px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

button:hover {
  background-color: #2980b9;
}

button:focus {
  outline: none;
}

/* Estilos para la barra de búsqueda */
.search-container {
  margin-bottom: 20px;
}

.search-container input {
  padding: 8px;
  width: 60%;
  max-width: 500px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 1rem;
}

/* Modal */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  width: 90%;
  max-width: 400px;
}

.modal-form-group {
  margin-bottom: 15px;
}

.modal-form-group label {
  display: block;
  margin-bottom: 5px;
}

.modal-form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-buttons {
  margin-top: 15px;
}

.modal-buttons button {
  margin: 5px;
  padding: 8px 16px;
}

/* Medios de comunicación (Responsividad) */
@media (max-width: 768px) {
  .search-container input {
    width: 80%;
  }

  table {
    font-size: 0.9rem;
  }

  .modal-content {
    width: 90%;
  }
}
</style>
