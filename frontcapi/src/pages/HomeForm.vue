<template>
    <div class="home-container">
      <div class="welcome-box">
        <h1>¡Bienvenido a la App!</h1>
        <p>Nos alegra verte. Por favor, inicia sesión para continuar.</p>
        
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
    background-color: #f0f8ff;  /* Fondo suave */
    font-family: 'Arial', sans-serif;  /* Fuente moderna */
    color: #333;
  }
  
  /* Caja de bienvenida */
  .welcome-box {
    text-align: center;
    background-color: white;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    max-width: 400px;
    width: 100%;
  }
  
  /* Estilo del título */
  .welcome-box h1 {
    font-size: 2.5rem;
    color: #2c3e50;
    margin-bottom: 1rem;
  }
  
  /* Estilo del mensaje */
  .welcome-box p {
    font-size: 1.1rem;
    margin-bottom: 1.5rem;
    color: #555;
  }
  
  /* Estilo del enlace de Login */
  .login-link {
    font-size: 1.2rem;
    color: #3498db;
    text-decoration: none;
    border: 2px solid #3498db;
    padding: 10px 20px;
    border-radius: 8px;
    transition: all 0.3s ease;
  }
  
  .login-link:hover {
    background-color: #3498db;
    color: white;
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
  