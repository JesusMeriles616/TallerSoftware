package com.example.keycloak.services;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.mapstruct.*;

import com.example.keycloak.dto.UserCreateDTO;
import com.example.keycloak.dto.UserDTO;
@Mapper(componentModel = "spring", imports = {CredentialRepresentation.class})
public interface keycloakconverter {
    @Mapping(target = "id",ignore = true)
    UserDTO todto(UserCreateDTO userCreateDTO);
    // Add this if you need the reverse mapping
    @InheritInverseConfiguration
    UserCreateDTO toCreateDto(UserDTO userDTO);
}
