import { createApp } from 'vue' // Importa 'createApp' desde 'vue'
import App from './App.vue'
import router from './router';
// Monta la aplicación
createApp(App)
  .use(router)
  .mount('#app');
