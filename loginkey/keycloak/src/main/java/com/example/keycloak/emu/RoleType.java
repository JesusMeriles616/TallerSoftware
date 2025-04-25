package com.example.keycloak.emu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
    // Nuevos roles
    PADRE("Padre", "Usuario con control sobre cuentas de niños", RealmRole.PADRE),
    NIÑO("Niño", "Usuario con acceso limitado", RealmRole.NIÑO),
    ADMINISTRADOR("Administrador", "Gestión avanzada del sistema", RealmRole.ADMINISTRADOR);
    
    private final String nombre;
    private final String descripcion;
    private final RealmRole realmRole;
}
