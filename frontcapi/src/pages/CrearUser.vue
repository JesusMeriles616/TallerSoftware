<template>
  <div class="create-user-container">
    <h2>Crear Nueva Cuenta</h2>

    <!-- Formulario de creación de cuenta -->
    <form @submit.prevent="handleCreateUser" class="create-user-form">
      <div class="form-group">
        <label for="nombre">Nombre:</label>
        <input 
          type="text" 
          id="nombre" 
          v-model="nombre" 
          placeholder="Introduce tu nombre completo" 
          required
        />
      </div>

      <div class="form-group">
        <label for="email">Correo electrónico:</label>
        <input 
          type="email" 
          id="email" 
          v-model="email" 
          placeholder="Introduce tu correo electrónico" 
          required
        />
      </div>

      <div class="form-group">
        <label for="password">Contraseña:</label>
        <input 
          type="password" 
          id="password" 
          v-model="password" 
          placeholder="Introduce una contraseña" 
          required
        />
      </div>

      <button type="submit" class="submit-btn">Crear cuenta</button>

      <!-- Mensajes de éxito o error -->
      <p v-if="createErrorMessage" class="error-message">{{ createErrorMessage }}</p>
      <p v-if="createSuccessMessage" class="success-message">{{ createSuccessMessage }}</p>
    </form>

    <hr />

    <!-- Enlace para redirigir a la página de login -->
    
  </div>
</template>

<script>
import UserService from "@/service/UserService";  

export default {
  name: "CrearUser",
  data() {
    return {
      nombre: "",
      email: "",
      password: "",
      createErrorMessage: "",
      createSuccessMessage: "",
    };
  },
  methods: {
    async handleCreateUser() {
      try {
        const userService = new UserService();
        
        await userService.createUser(this.nombre, this.email, this.password);
        
        this.createSuccessMessage = "Usuario creado exitosamente. ¡Ya puedes iniciar sesión!";
        this.createErrorMessage = "";
        
        setTimeout(() => {
          this.$router.push("/LoginForm");  
        }, 2000);
      } catch (error) {
        this.createErrorMessage = "Error al crear cuenta. Por favor, intenta de nuevo.";
        this.createSuccessMessage = "";
        console.error("Error al crear cuenta:", error);
      }
    },
  },
};
</script>

<style scoped>
/* Contenedor principal */
.create-user-container {
  width: 80%;
  max-width: 450px;
  margin: 0 auto;
  padding: 30px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  text-align: center;
}

/* Título */
h2 {
  color: #333;
  font-size: 1.8rem;
  margin-bottom: 20px;
}

/* Formulario */
.create-user-form {
  margin-top: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 1.1rem;
  color: #444;
}

.form-group input {
  width: 100%;
  padding: 12px;
  margin-top: 8px;
  border: 2px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input:focus {
  border-color: #3498db;
  outline: none;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background-color: #2980b9;
}

/* Estilos para los mensajes de error y éxito */
.error-message,
.success-message {
  margin-top: 20px;
  font-size: 1rem;
  font-weight: 500;
  transition: opacity 0.5s ease-in-out;
}

.error-message {
  color: red;
}

.success-message {
  color: green;
}

.login-text {
  margin-top: 20px;
}

.login-text a {
  color: #3498db;
  text-decoration: none;
}

.login-text a:hover {
  text-decoration: underline;
}
</style>
