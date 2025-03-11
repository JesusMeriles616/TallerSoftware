package com.juego.user.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.juego.user.dto.Login_dto;
import com.juego.user.dto.User_dto;
import com.juego.user.service.user_interface;

@RestController
@RequestMapping("/Users")
public class user_api {
    @Autowired
    private user_interface userService;

    // Crear un usuario (POST)
    @PostMapping("/crear")
    public ResponseEntity<String> createUser(@RequestBody User_dto user) {
        userService.createUser(user.getNombre(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok("Usuario creado exitosamente");
    }

    // Obtener todos los usuarios (GET)
    @GetMapping("/lista")
    public ResponseEntity<List<User_dto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Obtener un usuario por ID (GET)
    @GetMapping("/busqueda/{id}")
    public ResponseEntity<User_dto> getUserById(@PathVariable int id) {
        Optional<User_dto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un usuario (PUT)
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User_dto user) {
        boolean updated = userService.updateUser(id, user.getNombre(), user.getEmail(), user.getPassword());
        if (updated) {
            return ResponseEntity.ok("Usuario actualizado exitosamente");
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar un usuario (DELETE)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        }
        return ResponseEntity.notFound().build();
    }

    // Login (POST)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login_dto loginDto) {
        boolean loginExitoso = userService.login(loginDto.getEmail(), loginDto.getPassword());
        if (loginExitoso) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(401)
                    .body("Credenciales incorrectas. Intentos restantes: " + (3 - userService.getIntentosFallidos()));
        }
    }
}
