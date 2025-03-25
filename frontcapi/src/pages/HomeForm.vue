<template>
  <div class="home-container">
    <div class="welcome-box">
      <h1>¡Bienvenido a BlindColors!</h1>
      <p>Un programa enfocado en el apoyo a los niños con problemas al momento de identificar colores.</p>

      <!-- Sección de características -->
      <div class="features">
        <div class="feature">
          <img src="https://cdn-icons-png.flaticon.com/512/1995/1995485.png" alt="Icono de colores" />
          <h3>Identificación de colores</h3>
          <p>Aprende a identificar y diferenciar colores de manera divertida.</p>
        </div>
        <div class="feature">
          <img src="https://cdn-icons-png.flaticon.com/512/1995/1995488.png" alt="Icono de juegos" />
          <h3>Juegos interactivos</h3>
          <p>Juegos diseñados para mejorar la percepción del color.</p>
        </div>
        <div class="feature">
          <img src="https://cdn-icons-png.flaticon.com/512/1995/1995494.png" alt="Icono de progreso" />
          <h3>Seguimiento de progreso</h3>
          <p>Registra y visualiza el avance de los niños.</p>
        </div>
      </div>

      <!-- Llamado a la acción -->
      <div class="cta">
        <p>¿Listo para comenzar?</p>
        <router-link to="/registro" class="cta-button">Regístrate ahora</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomeForm",
};
</script>

<style scoped>
/* Contenedor principal */
.home-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #fff3e0;  /* Fondo naranja claro y suave */
  font-family: 'Comic Neue', cursive;  /* Fuente divertida y amigable */
  color: #333;
  padding: 20px;
}

/* Caja de bienvenida */
.welcome-box {
  text-align: center;
  background-color: #ffffff;  /* Fondo blanco para el contenedor */
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  width: 100%;
}

/* Estilo del título */
.welcome-box h1 {
  font-size: 2.5rem;
  color: #ff6f61;  /* Color coral para el título */
  margin-bottom: 1rem;
  font-family: 'Fredoka One', cursive;  /* Fuente divertida para el título */
}

/* Estilo del mensaje */
.welcome-box p {
  font-size: 1.1rem;
  margin-bottom: 1.5rem;
  color: #555;
  font-family: 'Comic Neue', cursive;  /* Fuente amigable para el texto */
}

/* Sección de características */
.features {
  display: flex;
  justify-content: space-around;
  margin-top: 2rem;
  flex-wrap: wrap;
}

.feature {
  flex: 1;
  margin: 1rem;
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 250px;
}

.feature img {
  width: 64px;
  height: 64px;
  margin-bottom: 1rem;
}

.feature h3 {
  font-size: 1.2rem;
  color: #ff6f61;
  margin-bottom: 0.5rem;
}

.feature p {
  font-size: 0.9rem;
  color: #555;
}

/* Llamado a la acción */
.cta {
  margin-top: 2rem;
}

.cta p {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 1rem;
}

.cta-button {
  font-size: 1.2rem;
  color: #ffffff;
  text-decoration: none;
  background-color: #ff6f61;
  padding: 10px 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-family: 'Comic Neue', cursive;
}

.cta-button:hover {
  background-color: #ff4a3d;
  transform: scale(1.1);
}

/* Efecto de animación de bienvenida */
.welcome-box h1 {
  animation: fadeIn 1.5s ease-in-out;
}

/* Animación de desvanecimiento */
@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>