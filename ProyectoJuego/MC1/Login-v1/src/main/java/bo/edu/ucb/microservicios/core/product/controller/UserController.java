package bo.edu.ucb.microservicios.core.product.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.edu.ucb.microservicios.core.product.dto.UserDto;
import bo.edu.ucb.microservicios.core.product.entity.User;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private List<User> users = new ArrayList<>();

    // Constructor para agregar datos iniciales (mock data)
    public UserController() {
        users.add(createMockUser("Juan", "Perez", "Admin", "juan.perez@example.com", "password123"));
        users.add(createMockUser("Maria", "Gomez", "User", "maria.gomez@example.com", "password456"));
        users.add(createMockUser("Carlos", "Lopez", "Editor", "carlos.lopez@example.com", "password789"));
    }

    // Método auxiliar para crear usuarios mock
    private User createMockUser(String nombre, String apellido, String rol, String correo, String password) {
        User user = new User();
        user.setUid(UUID.randomUUID().toString());
        user.setNombre(nombre);
        user.setApellido(apellido);
        user.setRol(rol);
        user.setCorreo(correo);
        user.setPassword(password);
        user.setFechaCreacion(new Date());
        return user;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        LOGGER.info("Obteniendo lista de usuarios");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserByUid(@PathVariable String uid) {
        LOGGER.info("Buscando usuario con UID: {}", uid);
        return users.stream()
                .filter(user -> user.getUid().equals(uid))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        LOGGER.info("Creando nuevo usuario");
        User user = new User();
        user.setUid(UUID.randomUUID().toString());
        user.setNombre(userDto.getNombre());
        user.setApellido(userDto.getApellido());
        user.setRol(userDto.getRol());
        user.setCorreo(userDto.getCorreo());
        user.setPassword(userDto.getPassword());
        user.setFechaCreacion(new Date());

        users.add(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<User> updateUser(@PathVariable String uid, @RequestBody UserDto userDto) {
        LOGGER.info("Actualizando usuario con UID: {}", uid);
        return users.stream()
                .filter(user -> user.getUid().equals(uid))
                .findFirst()
                .map(user -> {
                    user.setNombre(userDto.getNombre());
                    user.setApellido(userDto.getApellido());
                    user.setRol(userDto.getRol());
                    user.setCorreo(userDto.getCorreo());
                    user.setPassword(userDto.getPassword());
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uid) {
        LOGGER.info("Eliminando usuario con UID: {}", uid);
        boolean removed = users.removeIf(user -> user.getUid().equals(uid));
        if (removed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}