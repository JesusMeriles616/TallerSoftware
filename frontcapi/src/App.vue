<template>
  <div id="app">
    <!-- Barra de navegación -->
    <Navbar />

    <!-- Contenido dinámico de las rutas -->
    <router-view></router-view>

    <!-- Footer principal -->
    <FooterApp />
    
    <!-- Footer secundario -->
    <FooterView />
  </div>
</template>

<script>
import Navbar from "@/components/NavBar.vue";
import FooterView from "@/components/FooterView.vue";

export default {
  name: "App",
  components: {
    Navbar,
    FooterView,
  },
};
</script>

<style>
/* Estilos globales opcionales */
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

router-view {
  flex: 1;
}
</style>
