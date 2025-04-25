package com.example.keycloak.services;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import com.example.keycloak.dto.UserCreateDTO;
import com.example.keycloak.dto.UserDTO;
//@Mapper(componentModel = "spring", imports = {CredentialRepresentation.class})
@Component
public class keycloakconverter {

    //@Mapping(target = "id",ignore = true)
    //UserDTO todto(UserCreateDTO userCreateDTO);
    // Add this if you need the reverse mapping
    //@InheritInverseConfiguration
   // UserCreateDTO toCreateDto(UserDTO userDTO);
     // Convierte UserCreateDTO a UserDTO
     public UserDTO toUserDTO(UserCreateDTO userCreateDTO) {
        return UserDTO.builder()
                  // Establece el ID del usuario
                .username(userCreateDTO.getUsername())
                .email(userCreateDTO.getEmail())
                .firstName(userCreateDTO.getFirstName())
                .lastName(userCreateDTO.getLastName())
                .password(userCreateDTO.getPassword()) // Ten cuidado con la contraseña
                .roles(userCreateDTO.getRoles())
                .enabled(userCreateDTO.isEnabled())
                .emailVerified(userCreateDTO.isEmailVerified())
                .build();
    }

    // Convierte UserDTO a UserCreateDTO si es necesario
    public UserCreateDTO toUserCreateDTO(UserDTO userDTO) {
        return UserCreateDTO.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .password(userDTO.getPassword()) // Ten cuidado con la contraseña
                .roles(userDTO.getRoles())
                .enabled(userDTO.isEnabled())
                .emailVerified(userDTO.isEmailVerified())
                .build();
    }
}
