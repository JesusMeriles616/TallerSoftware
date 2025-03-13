<template>
    <div class="login-container">
      <h2>Iniciar sesión</h2>
  
      <!-- Formulario de inicio de sesión -->
      <form @submit.prevent="handleLogin" class="login-form">
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
            placeholder="Introduce tu contraseña" 
            required
          />
        </div>
  
        <button type="submit" class="submit-btn">Iniciar sesión</button>
  
        <p v-if="loginErrorMessage" class="error-message">{{ loginErrorMessage }}</p>
      </form>
  
      <hr />
  
      <!-- Enlace para redirigir a la página de crear cuenta -->
      <p class="create-account-text">
        ¿No tienes cuenta? <router-link to="/CrearUser">Crear usuario nuevo</router-link>
      </p>
    </div>
  </template>
  <script>
import axios from "axios";

export default {
  name: "LoginForm",
  data() {
    return {
      email: "",
      password: "",
      loginErrorMessage: ""
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post("http://localhost:400/Users/login", {
          email: this.email,
          password: this.password,
        });

        console.log("Respuesta de backend (login):", response.data);

        // Si el inicio de sesión es exitoso, redirigir a /listaForm
        this.$router.push("/listaForm");

      } catch (error) {
        this.loginErrorMessage = "Error al iniciar sesión. Verifica tus credenciales.";
        console.error("Error al hacer login:", error);
      }
    }
  }
};
</script>

  
  <style scoped>
  /* Estilos para el formulario de login */
  .login-container {
    width: 100%;
    max-width: 500px;
    margin: 0 auto;
    text-align: center;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    margin-bottom: 20px;
    color: #333;
  }
  
  .form-group {
    margin-bottom: 15px;
    text-align: left;
  }
  
  label {
    font-size: 1.1rem;
    color: #555;
  }
  
  input {
    width: 100%;
    padding: 10px;
    margin-top: 5px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  button.submit-btn {
    width: 100%;
    padding: 12px;
    background-color: #3498db;
    color: white;
    font-size: 1.1rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  button.submit-btn:hover {
    background-color: #2980b9;
  }
  
  .error-message {
    color: red;
    font-size: 1rem;
    margin-top: 20px;
  }
  
  hr {
    margin: 30px 0;
  }
  
  .create-account-text {
    font-size: 1rem;
    margin-top: 20px;
  }
  
  .create-account-text a {
    color: #3498db;
    text-decoration: none;
  }
  
  .create-account-text a:hover {
    text-decoration: underline;
  }
  </style>
  