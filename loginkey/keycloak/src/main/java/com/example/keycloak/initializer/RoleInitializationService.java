package com.example.keycloak.initializer;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.example.keycloak.emu.RealmRole;
import com.example.keycloak.emu.RoleType;
import com.example.keycloak.util.keycloakProvider;
import com.example.keycloak.util.KeycloakServiceException;
import java.util.*;
import java.util.stream.Collectors;
/*
 * Servicio para la inicialización de roles en Keycloak al arrancar la aplicación.
 * Crea roles de cliente y realm, y establece relaciones de composición entre ellos.
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RoleInitializationService {
        private static final String CLIENT_ID = "spring_juegos";
        private final keycloakProvider keycloakProvider;

        @EventListener(ApplicationReadyEvent.class)
        public void initializeKeycloakRoles() {
            log.info("Iniciando verificación de roles en Keycloak...");

            try (Keycloak keycloakAdmin = keycloakProvider.createKeycloakAdminInstance()) {
                RealmResource realmResource = keycloakAdmin.realm(keycloakProvider.getRealm());

                // First initialize client roles
                initializeClientRoles(realmResource);
                // Then realm roles with their composites
                initializeRealmRoles(realmResource);

                log.info("Verificación de roles completada exitosamente");
            } catch (KeycloakServiceException e) {
                throw e; // Relanzar excepciones conocidas
            } catch (Exception e) {
                String errorMsg = "Error durante la inicialización de roles en Keycloak";
                log.error(errorMsg, e);
                throw new KeycloakServiceException(errorMsg, e);
            }
        }

        private void initializeRealmRoles(RealmResource realmResource) {
            RolesResource realmRolesResource = realmResource.roles();
            Map<RealmRole, List<RoleType>> rolesByRealm = buildRealmRoleMapping();

            Arrays.stream(RealmRole.values()).parallel()
                    .forEach(realmRole -> processRealmRole(realmRolesResource, realmResource, realmRole, rolesByRealm));
        }
        private void processRealmRole(RolesResource realmRolesResource, RealmResource realmResource,
                                      RealmRole realmRole, Map<RealmRole, List<RoleType>> rolesByRealm) {
            String roleName = realmRole.getRoleName();
            log.debug("Procesando rol: {}", roleName);

            ensureRealmRoleExists(realmRolesResource, realmRole);

            if (rolesByRealm.containsKey(realmRole)) {
                addCompositesToRealmRole(realmResource, realmRole, rolesByRealm.get(realmRole));
            }
        }
        private Map<RealmRole, List<RoleType>> buildRealmRoleMapping() {
            return Arrays.stream(RoleType.values())
                    .filter(roleType -> roleType.getRealmRole() != null)
                    .collect(Collectors.groupingBy(
                            RoleType::getRealmRole,
                            Collectors.toList()
                    ));
        }

        private void ensureRealmRoleExists(RolesResource rolesResource, RealmRole realmRole) {
            if (!roleExists(rolesResource, realmRole.getRoleName())) {
                createRole(rolesResource, realmRole.getRoleName(), realmRole.getDescription());
                log.info("Realm role creado: {}", realmRole.getRoleName());
            } else {
                log.info("El rol ya existe: {}", realmRole.getRoleName());
            }
        }

        private void initializeClientRoles(RealmResource realmResource) {
            String clientUuid = getClientUuid(realmResource);
            RolesResource clientRolesResource = realmResource.clients().get(clientUuid).roles();

            Arrays.stream(RoleType.values()).parallel()
                    .forEach(roleType -> ensureClientRoleExists(clientRolesResource, roleType));
        }
        private void ensureClientRoleExists(RolesResource clientRolesResource, RoleType roleType) {
            if (!roleExists(clientRolesResource, roleType.name())) {
                createRole(clientRolesResource, roleType.name(), roleType.getDescripcion());
                log.info("Client role creado: {}", roleType.name());
            } else {
                log.info("Client role ya existe: {}", roleType.name());
            }
        }

        private void addCompositesToRealmRole(RealmResource realmResource,
                                              RealmRole realmRole,
                                              List<RoleType> roleTypes) {
            String clientUuid = getClientUuid(realmResource);
            RolesResource clientRolesResource = realmResource.clients().get(clientUuid).roles();

            List<RoleRepresentation> clientRoles = roleTypes.stream()
                    .map(RoleType::name)
                    .map(roleName  -> getClientRoleRepresentation(clientRolesResource,roleName))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!clientRoles.isEmpty()) {
                realmResource.roles().get(realmRole.getRoleName()).addComposites(clientRoles);
                log.debug("Roles compuestos agregados a {}: {}",
                        realmRole.getRoleName(),
                        clientRoles.stream().map(RoleRepresentation::getName).collect(Collectors.joining(", ")));
            }
        }

        private RoleRepresentation getClientRoleRepresentation(RolesResource rolesResource, String roleName) {
            try {
                return rolesResource.get(roleName).toRepresentation();
            } catch (NotFoundException e) {
                log.warn("Client role no encontrado: {}", roleName);
                return null;
            } catch (Exception e) {
                log.error("Error obteniendo client role: {}", roleName, e);
                return null;
            }
        }


        private String getClientUuid(RealmResource realmResource) {
            return realmResource.clients()
                    .findByClientId(CLIENT_ID)
                    .stream()
                    .findFirst()
                    .map(ClientRepresentation::getId)
                    .orElseThrow(() -> new KeycloakServiceException(
                            "Client '" + CLIENT_ID + "' no encontrado en Keycloak"));
        }

        private boolean roleExists(RolesResource rolesResource, String roleName) {
            try {
                rolesResource.get(roleName).toRepresentation();
                return true;
            } catch (NotFoundException e) {
                return false;
            }
        }

        private void createRole(RolesResource rolesResource,
                                String name,
                                String description) {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(name);
            role.setDescription(description);
            role.setComposite(false);

            try {
                rolesResource.create(role);
            } catch (Exception e) {
                throw new KeycloakServiceException("Error creando role: " + name, e);
            }
        }
}