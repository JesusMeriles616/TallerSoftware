package com.example.keycloak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.keycloak.services.keycloakinterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/loginapi")
public class api {

    @Autowired
    private keycloakinterface keycloakinterface;
    



}
