package com.example.keycloak.emu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RealmRole {
 
    // Nuevos roles si es necesario
    PADRE("padre", "Usuario con rol de padre o tutor"),
    NIÑO("niño", "Usuario infantil con permisos limitados"),
    ADMINISTRADOR("administrador", "Administrador general del sistema");
    
    private final String roleName;
    private final String description;
}
