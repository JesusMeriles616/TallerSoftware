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
/* Estilos para el fondo de la página */
body {
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  font-family: 'Nunito', sans-serif;
  background-image: url('https://images.vexels.com/media/users/3/333316/raw/ce7538c3d61191c8132fff125b0a8af3-diseno-de-patrones-de-capibaras-y-flores.jpg'); /* URL de la imagen de fondo */
  background-size: cover; /* Cubre toda la pantalla */
  background-color: #bd732d;
  background-position: center; /* Centra la imagen */
  background-repeat: no-repeat; /* Evita que se repita la imagen */
}

/* Estilos para el contenedor del formulario */
.login-container {
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
  text-align: center;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.9); /* Fondo blanco con transparencia */
  border-radius: 12px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  font-family: 'Nunito', sans-serif;
}

h2 {
  margin-bottom: 20px;
  color: #ff6f61; /* Color coral para el título */
  font-size: 2rem;
  font-family: 'Comic Sans MS', cursive;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

label {
  font-size: 1.1rem;
  color: #555; /* Color gris oscuro para las etiquetas */
  font-family: 'Nunito', sans-serif;
}

input {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-family: 'Nunito', sans-serif;
  background-color: #ffffff; /* Fondo blanco para los inputs */
}

button.submit-btn {
  width: 100%;
  padding: 12px;
  background-color: #ff6f61; /* Color coral para el botón */
  color: white;
  font-size: 1.1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-family: 'Comic Sans MS', cursive;
}

button.submit-btn:hover {
  background-color: #ff4a3d; /* Color coral más oscuro al hacer hover */
}

.error-message {
  color: #ff4a3d; /* Color coral oscuro para mensajes de error */
  font-size: 1rem;
  margin-top: 20px;
  font-family: 'Nunito', sans-serif;
}

hr {
  margin: 30px 0;
  border: 0;
  border-top: 1px solid #bd732d;
}

.create-account-text {
  font-size: 1rem;
  margin-top: 20px;
  font-family: 'Nunito', sans-serif;
  color: #555; /* Color gris oscuro para el texto */
}

.create-account-text a {
  color: #ff6f61; /* Color coral para el enlace */
  text-decoration: none;
}

.create-account-text a:hover {
  text-decoration: underline; /* Subrayado al hacer hover */
}
</style>