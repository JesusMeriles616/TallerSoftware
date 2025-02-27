<?php
header('Content-Type: application/json');
require 'database_connection.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $input = json_decode(file_get_contents("php://input"), true);
    $id_user = $input['id_user'] ?? null;
    $difficulty = $input['difficulty'] ?? null;
    $isCompleted = $input['isCompleted'] ?? null;
    $score = $input['score'] ?? null;

    if (!$id_user || !$difficulty || $isCompleted === null || $score === null) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Datos de juego faltantes"]);
        exit();
    }

    $connection = connect();
    $insert_query = "INSERT INTO game (id_player, difficulty, is_completed, score, started_at) VALUES (?, ?, ?, ?, NOW())";
    $stmt = $connection->prepare($insert_query);
    $stmt->bind_param("isii", $id_user, $difficulty, $isCompleted, $score);

    if ($stmt->execute()) {
        http_response_code(201);
        echo json_encode(["success" => true, "id_game" => $stmt->insert_id]);
    } else {
        http_response_code(500);
        echo json_encode(["success" => false, "message" => "Fallo en crear nuevo juego"]);
    }

    $stmt->close();
    $connection->close();
}
?>
