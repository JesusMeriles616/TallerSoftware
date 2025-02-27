CREATE DATABASE color_blind;


USE color_blind;


CREATE TABLE player (
    id_player INT AUTO_INCREMENT PRIMARY KEY,  -- INT data type with AUTO_INCREMENT
    user_name VARCHAR(50) NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT NOW()
);

CREATE TABLE game (
    id_game INT AUTO_INCREMENT PRIMARY KEY,  -- INT for primary key
    id_player INT NOT NULL,  -- INT data type for foreign key
    difficulty VARCHAR(25) NOT NULL,
    is_completed INT NOT NULL,
    score int NOT NULL,
    started_at DATETIME DEFAULT NOW(),
    FOREIGN KEY (id_player) REFERENCES player(id_player) ON DELETE CASCADE  -- Foreign key reference
);

CREATE TABLE color (
    id_color INT AUTO_INCREMENT PRIMARY KEY,
    color VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT NOW()
);

CREATE TABLE color_game (
    id_color_game INT AUTO_INCREMENT PRIMARY KEY,
    id_game INT NOT NULL,
    id_color INT NOT NULL,
    color VARCHAR(50) NOT NULL,
    is_succeeded INT NOT NULL,
    created_at DATETIME DEFAULT NOW(),
    FOREIGN KEY (id_game) REFERENCES game(id_game) ON DELETE CASCADE,
    FOREIGN KEY (id_color) REFERENCES color(id_color) ON DELETE CASCADE
);

INSERT INTO player (user_name, pwd) 
VALUES 
('1', '1'),
('2', '2'),
('3', '3'),
('4', '4'),
('5', '5');

INSERT INTO color (color)
VALUES 
('ROJO'),
('AZUL'),
('VERDE'),
('ROSADO'),
('NEGRO'),
('AMARILLO'),
('NARANJA'),
('BLANCO'),
('MORADO'),
('CAFE');



