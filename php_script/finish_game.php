<?php 
require 'database_connection.php';

header('Content-Type: application/json'); // Set the response type to JSON

if ($_SERVER["REQUEST_METHOD"] == "PUT") {

    // Retrieve JSON input
    $input = json_decode(file_get_contents("php://input"), true);

    // Check if JSON decoding succeeded and required fields are present
    if (json_last_error() !== JSON_ERROR_NONE || empty($input['id_game']) || !isset($input['score']) || !isset($input['is_completed'])) {
        http_response_code(400);  // Bad Request
        echo json_encode(["success" => false, "message" => "Error con id de juego, puntaje o estado de finalización"]);
        exit;
    }

    $id_game = htmlspecialchars($input['id_game']);
    $score = htmlspecialchars($input['score']);
    $is_completed = htmlspecialchars($input['is_completed']);

    // Get database connection
    $conn = getDatabaseConnection();

    if ($conn) {
        // SQL Query to update the game details
        $sql = "UPDATE game SET score = :score, is_completed = :is_completed WHERE id_game = :id_game";

        $stmt = $conn->prepare($sql);

        try {
            // Execute the update statement with the provided data
            $stmt->execute([
                'id_game' => $id_game,
                'score' => $score,
                'is_completed' => $is_completed
            ]);

            // Check if the update was successful
            if ($stmt->rowCount() > 0) {
                http_response_code(200);  // OK
                echo json_encode([
                    "success" => true,
                    "message" => "Juego actualizado correctamente."
                ]);
            } else {
                http_response_code(404);  // Not Found
                echo json_encode([
                    "success" => false,
                    "message" => "Juego no encontrado o no se realizaron cambios."
                ]);
            }
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
