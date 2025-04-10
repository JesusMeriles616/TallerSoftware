package bo.edu.ucb.microservicios.core.api;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "API endpoints for user management operations")
public class UserApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserApi.class);
    private List<User> users = new ArrayList<>();

    // Constructor para agregar datos iniciales (mock data)
    public UserApi() {
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

    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
            content = @Content(schema = @Schema(implementation = User.class)))
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        LOGGER.info("Obteniendo lista de usuarios");
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by UID", description = "Retrieves a specific user by their UID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found successfully",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserByUid(
            @Parameter(description = "UID of the user to retrieve", required = true)
            @PathVariable String uid) {
        LOGGER.info("Buscando usuario con UID: {}", uid);
        return users.stream()
                .filter(user -> user.getUid().equals(uid))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = User.class)))
    })
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "User details for creation", required = true)
            @RequestBody UserDto userDto) {
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

    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{uid}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "UID of the user to update", required = true)
            @PathVariable String uid,
            @Parameter(description = "Updated user details", required = true)
            @RequestBody UserDto userDto) {
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

    @Operation(summary = "Delete user", description = "Deletes a user from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "UID of the user to delete", required = true)
            @PathVariable String uid) {
        LOGGER.info("Eliminando usuario con UID: {}", uid);
        boolean removed = users.removeIf(user -> user.getUid().equals(uid));
        if (removed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}