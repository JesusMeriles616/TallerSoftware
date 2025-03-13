import { createRouter, createWebHistory } from "vue-router";
import HomeForm from "./pages/HomeForm.vue";
import LoginForm from "./components/LoginForm.vue";
import ListForm from "./pages/ListForm.vue";
import CrearUser from "./pages/CrearUser.vue";

const routes = [
  {
    path: '/', 
    redirect: '/HomeForm'  // Redirige a HomeForm si la ruta es la raíz
  },
  {
    path: '/HomeForm', 
    name: 'HomeForm',
    component: HomeForm  // Cuando la ruta es /HomeForm, carga HomeForm
  },
  {
    path: '/LoginForm',
    name: 'LoginForm',
    component: LoginForm  // Cuando la ruta es /LoginForm, carga LoginForm
  },
  {
    path:'/ListaForm',
    name:'ListaForm',
    component:ListForm
  },
  {
    path:'/CrearUser',
    name:'CrearUser',
    component:CrearUser
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;
