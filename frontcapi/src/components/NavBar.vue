<template>
   <nav class="navbar" role="navigation" aria-label="main navigation">
  <div class="navbar-brand">
    <div class="navbar-item" @click="navigateTo('home')" style="flex-grow: 1; justify-content: center; display: flex; align-items: center;">
      <img src="@/assets/capibara.jpg" alt="Logo" style="max-height: 80px; margin-right: 10px;">
      <div class="rainbow-text">
        <h3>Grupo Capibara</h3>
      </div>
    </div>
  </div>
  <p class="rainbow">JUEGO COLORES</p> <!-- Aquí agregamos el texto centrado -->
  <div class="navbar-menu" :class="{ 'is-active': isMenuActive }">
    <div class="navbar-start">
      <div class="auth-buttons">
        <router-link to="/LoginForm" class="auth-button">Login</router-link>
        <button @click="closeSession" class="auth-button">Cerrar Sesión</button>
      </div>
    </div>
  </div>
</nav>

  </template>
  
  
  <script>
  export default {
    data() {
      return {
        isMenuActive: false
      };
    },
    methods: {
      closeSession() {
        localStorage.clear();
        this.$router.push('/');
      }
    }
  };
  </script>
  
  <style scoped>
  nav {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 30px;
    background-color: #79bfd4; /* Fondo azul */
    border-bottom: 8px solid #116eba; /* Línea azul debajo del navbar */
    color: #4a4a4a;
  }
  
  .navbar-item {
    color: #dee6eb; /* Asegúrate de que los ítems sean visibles sobre fondo azul */
    cursor: pointer;
  }
  
  .navbar-item:hover {
    color: #dc14cb; /* Color cuando el usuario pasa el mouse */
  }
  
  .icon {
    color: #1b1010; /* Ajusta el color de los íconos */
  }
  
  button {
    background-color: transparent;
    border: none;
    color: #1fc4c4;
    cursor: pointer; /* Asegúrate de que el cursor sea de tipo pointer en los botones */
  }
  
  button:hover {
    color: #ff00e6;
  }
  
  /* Estilos para los botones Login y Cerrar Sesión */
  .auth-buttons {
    display: flex;
    justify-content: space-around; /* Los botones estarán alineados horizontalmente */
    gap: 20px; /* Espacio entre los botones */
  }
  
  .auth-button {
    background-color: #aaaaa8;
    border: none;
    color: #116eba;
    padding: 20px 50px; /* Aumentamos el tamaño de los botones */
    font-size: 20px; /* Aumentamos el tamaño de la fuente */
    font-weight: bold; /* Hacemos el texto más grueso */
    text-align: center;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  .auth-button:hover {
    background-color: #5b5855;
    transform: scale(1.05); /* Agrandar ligeramente al pasar el mouse */
  }
  
  /* Estilos para el navbar burger */
  .navbar-burger span {
    display: block;
    height: 3px;
    background-color: #4a4a4a;
    margin: 5px auto;
    transition: transform 0.3s ease;
  }
  
  /* Ajustes para pantallas pequeñas */
  @media (max-width: 768px) {
    .navbar-menu {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      background-color: #fff;
      position: absolute;
      top: 70px;
      left: 0;
      right: 0;
      padding: 10px 0;
      transition: transform 0.3s ease;
      transform: translateY(-100%);
    }
  
    .navbar-menu.is-active {
      transform: translateY(0);
    }
  
    /* En pantallas pequeñas los botones estarán en columna */
    .auth-buttons {
      flex-direction: column;
    }
  
    .auth-button {
      font-size: 18px;
      padding: 15px 40px;
    }
  }
  .auth-button {
  font-size: 16px;
  padding: 10px 20px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-transform: uppercase;
  text-align: center;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.auth-button:hover {
  background-color: #2980b9;
}

.auth-button {
  font-size: 16px;
  padding: 10px 20px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  position: relative;
  animation: glowing 1.5s infinite ease-in-out alternate;
}

/* Efecto de brillo */
@keyframes glowing {
  0% {
    text-shadow: 0 0 5px #fff, 0 0 10px #3498db, 0 0 15px #3498db, 0 0 20px #3498db;
  }
  50% {
    text-shadow: 0 0 10px #fff, 0 0 20px #3498db, 0 0 30px #3498db, 0 0 40px #3498db;
  }
  100% {
    text-shadow: 0 0 5px #fff, 0 0 10px #3498db, 0 0 15px #3498db, 0 0 20px #3498db;
  }
}

/* Efecto de movimiento en hover */
.auth-button:hover {
  animation: movingButton 1s ease-in-out infinite;
}

@keyframes movingButton {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}
.rainbow {
  font-size: 20px;
  font-weight: bold;
  animation: rainbow 4s infinite linear;
  text-align: center;
  width: 60%;
  margin: 0; /* Para evitar márgenes adicionales que afecten el posicionamiento */
}
@keyframes rainbow {
  0% {
    color: #ff0000; /* Rojo */
  }
  15% {
    color: #ff7f00; /* Naranja */
  }
  30% {
    color: #ffff00; /* Amarillo */
  }
  45% {
    color: #00ff00; /* Verde */
  }
  60% {
    color: #0000ff; /* Azul */
  }
  75% {
    color: #4b0082; /* Índigo */
  }
  100% {
    color: #9400d3; /* Violeta */
  }
  
}

  </style>
  