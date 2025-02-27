<?php 
require 'database_connection.php';

header('Content-Type: application/json'); // Set the response type to JSON

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    // Retrieve JSON input
    $input = json_decode(file_get_contents("php://input"), true);

    // Check if JSON decoding succeeded and required fields are present
    if (json_last_error() !== JSON_ERROR_NONE || empty($input['id_player']) || empty($input['difficulty'])) {
        http_response_code(400);  // Bad Request
        echo json_encode(["success" => false, "message" => "Error con id de jugador"]);
        exit;
    }
    $id_player = htmlspecialchars($input['id_player']); 
    $difficulty = htmlspecialchars($input['difficulty']); 

    // Get database connection
    $conn = getDatabaseConnection();

    if ($conn) {
        // Query to select the user with an exact match for username and password
        
        $sql = "INSERT INTO game (id_player, difficulty, is_completed, score) VALUES (:id_player, :difficulty, -1, -1)";
        $stmt = $conn->prepare($sql);

        try {
            // Execute the statement with the provided data
            $stmt->execute(['id_player' => $id_player, 'difficulty' => $difficulty]);
            // Get the id of the last inserted game
            $id_game = $conn->lastInsertId();
            http_response_code(201);  // Created
            echo json_encode([
                "success" => true,
                "message" => "Juego creado correctamente.",
                "id_game" => $id_game  // Include the id_game in the response
            ]);
        } catch (PDOException $e) {
            // SQL execution error
            http_response_code(500);  // Internal Server Error
            echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
        }
    } else {
        // Database connection error
        http_response_code(500);  // Internal Server Error
        echo json_encode(["success" => false, "message" => "Conexión a la base de datos incorrecta."]);
    }
} else {
    // Invalid request method
    http_response_code(405);  // Method Not Allowed
    echo json_encode(["success" => false, "message" => "Método no permitido."]);
}



?>
