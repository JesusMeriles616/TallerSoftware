<template>
    <footer class="footer">
      <div class="content has-text-centered">
        <p><strong>JUEGOS</strong></p>
      </div>
  
      <div class="footer-social">
        <p>Síguenos en:</p>
        <li><a href="mailto:info@ejemplo.com">oftamologiaespecial@gmail.com</a></li>
        <ul class="social-icons">
          <li v-for="(link, index) in socialLinks" :key="index">
            <a :href="link.url" target="_blank" :aria-label="link.name">
              <i :class="link.iconClass"></i>
            </a>
          </li>
        </ul>
      </div>
    </footer>
  </template>
  
  <script>
  
  </script>
  
  <style scoped>
  .footer {
    background-color: #116eba;
    color: #fff;
    padding: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    flex-direction: column;
  }
  
  .footer-social,
  .content {
    text-align: center;
  }
  
  .footer-social ul,
  .footer-social li {
    list-style: none;
    padding: 0;
  }
  
  .footer-social li {
    margin-bottom: 10px;
  }
  
  .footer-social i {
    font-size: 24px;
    margin-right: 10px;
    transition: transform 0.3s ease, color 0.3s ease; /* Transición suave */
  }
  
  .footer-social i:hover {
    transform: scale(1.2); /* Aumenta el tamaño del ícono */
    color: #ffcc00; /* Cambia el color al pasar el mouse */
  }
  
  .footer a {
    color: #fff;
    text-decoration: none;
  }
  
  .footer a:hover {
    text-decoration: underline;
  }
  
  .social-icons {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 180px; /* Ajustado para que no se estire demasiado */
    margin-top: 10px;
  }
  
  @media (max-width: 768px) {
    .social-icons {
      width: 120px;
    }
  
    .footer-social li {
      margin-bottom: 15px;
    }
  }
  </style>
  