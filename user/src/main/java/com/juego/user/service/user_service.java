package com.juego.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.juego.user.dto.User_dto;

@Service
public class user_service implements user_interface {
    private List<User_dto> users = new ArrayList<>();
    private int n = 1;
    private int intentosFallidos = 0;

    @Override
    public void createUser(String nombre, String email, String password) {
        User_dto user = new User_dto(n++, nombre, email, password);
        users.add(user);
    }

    @Override
    public List<User_dto> getAllUsers() {
        return users;
    }

    @Override
    public Optional<User_dto> getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public boolean updateUser(int id, String nombre, String email, String password) {
        Optional<User_dto> userOptional = getUserById(id);
        if (userOptional.isPresent()) {
            User_dto user = userOptional.get();
            user.setNombre(nombre);
            user.setEmail(email);
            user.setPassword(password);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

    @Override
    public boolean login(String email, String password) {
        for (User_dto user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                intentosFallidos = 0; // Reiniciar contador si el login es exitoso
                return true; // Login exitoso
            }
        }
        intentosFallidos++; // Incrementar contador si el login falla
        if (intentosFallidos >= 3) {
            cerrarAplicacion(); // Cerrar la aplicación después de 3 intentos fallidos
        }
        return false; // Login fallido
    }

    private void cerrarAplicacion() {
        System.out.println("Demasiados intentos fallidos. Cerrando la aplicación...");
        System.exit(1); // Cierra la aplicación con código de salida 1
    }

    @Override
    public int getIntentosFallidos() {
        return intentosFallidos;
    }
}
