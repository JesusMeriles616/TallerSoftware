package com.juego.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User_dto {
    private int id;
    private String nombre;
    private String email;
    private String password;
}
