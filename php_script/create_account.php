<?php
header('Content-Type: application/json');
require 'database_connection.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $input = json_decode(file_get_contents("php://input"), true);
    $username = $input['username'] ?? null;
    $pwd = $input['pwd'] ?? null;

    if (!$username || !$pwd) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Falta usuario o password"]);
        exit();
    }

    $connection = connect();

    $check_query = "SELECT * FROM player WHERE user_name = ?";
    $stmt = $connection->prepare($check_query);
    $stmt->bind_param("s", $username);
    $stmt->execute();
    if ($stmt->get_result()->num_rows > 0) {
        http_response_code(409);
        echo json_encode(["success" => false, "message" => "Usuario ya existe"]);
        $stmt->close();
        $connection->close();
        exit();
    }

    $stmt->close();
    $insert_query = "INSERT INTO player (user_name, pwd, created_at) VALUES (?, ?, NOW())";
    $stmt = $connection->prepare($insert_query);
    $stmt->bind_param("ss", $username, $pwd);

    if ($stmt->execute()) {
        http_response_code(201);
        echo json_encode(["success" => true, "message" => "Cuenta creada exitosamente"]);
    } else {
        http_response_code(500);
        echo json_encode(["success" => false, "message" => "Fallo para crear una cuenta"]);
    }

    $stmt->close();
    $connection->close();
}
?>