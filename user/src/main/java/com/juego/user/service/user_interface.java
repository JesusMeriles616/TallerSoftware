package com.juego.user.service;

import java.util.List;
import java.util.Optional;

import com.juego.user.dto.User_dto;

public interface user_interface {

    void createUser(String nombre, String email, String password);

    List<User_dto> getAllUsers();

    Optional<User_dto> getUserById(int id);

    boolean updateUser(int id, String nombre, String email, String password);

    boolean deleteUser(int id);

    boolean login(String email, String password);

    int getIntentosFallidos();
}
