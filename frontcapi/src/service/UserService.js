import axios from 'axios';

export default class UserService {
    url = "http://localhost:400/Users/";

    // Obtener todos los usuarios
    getAll() {
        return axios.get(this.url + "lista");
    }

    // Crear un usuario
    createUser(nombre, email, password) {
        return axios.post(this.url + "crear", { nombre, email, password });
    }

    // Actualizar un usuario
    updateUser(id, user) {
        return axios.put(`${this.url}actualizar/${id}`, user);
    }

    // Eliminar un usuario
    deleteUser(id) {
        return axios.delete(`${this.url}eliminar/${id}`);
    }
}
