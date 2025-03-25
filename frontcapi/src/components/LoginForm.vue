<template>
  <div class="login-container">
    <h2>🌟 Bienvenido Aventurero 🌟</h2>
    <p class="welcome-text">¡Ingresa a tu cuenta y prepárate para una gran aventura!</p>

    <form @submit.prevent="handleLogin" class="login-form">
      <div class="form-group">
        <label for="email">📧 Correo electrónico:</label>
        <input 
          type="email" 
          id="email" 
          v-model="email" 
          placeholder="Introduce tu correo electrónico" 
          required
        />
      </div>

      <div class="form-group">
        <label for="password">🔑 Contraseña:</label>
        <input 
          type="password" 
          id="password" 
          v-model="password" 
          placeholder="Introduce tu contraseña" 
          required
        />
      </div>

      <button type="submit" class="submit-btn">🚀 Iniciar sesión</button>
      <p v-if="loginErrorMessage" class="error-message">⚠️ {{ loginErrorMessage }}</p>
    </form>

    <hr />
    <p class="create-account-text">
      ¿No tienes cuenta? 🏰 <router-link to="/CrearUser">Crear usuario nuevo</router-link>
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
.login-container {
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
  text-align: center;
  padding: 30px;
  background: linear-gradient(135deg, #ff9a9e, #fad0c4);
  border-radius: 15px;
  box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.3);
}

h2 {
  color: #ffffff;
  font-size: 1.8rem;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.welcome-text {
  font-size: 1.1rem;
  color: #fff;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

label {
  font-size: 1.1rem;
  color: #fff;
}

input {
  width: 100%;
  padding: 12px;
  margin-top: 5px;
  font-size: 1rem;
  border: none;
  border-radius: 8px;
  background: #ffffff;
  color: #333;
}

input:focus {
  outline: none;
  box-shadow: 0 0 8px #ff758c;
}

button.submit-btn {
  width: 100%;
  padding: 12px;
  background: #ff758c;
  color: white;
  font-size: 1.2rem;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: transform 0.2s, background 0.3s ease;
}

button.submit-btn:hover {
  background: #ff5a79;
  transform: scale(1.05);
}

.error-message {
  color: #e74c3c;
  font-size: 1rem;
  margin-top: 10px;
}

hr {
  margin: 25px 0;
  border: 1px solid #fff;
}

.create-account-text {
  font-size: 1.1rem;
  color: #fff;
}

.create-account-text a {
  color: #ff758c;
  text-decoration: none;
  font-weight: bold;
}

.create-account-text a:hover {
  text-decoration: underline;
}
</style>