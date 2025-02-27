<?php 
require 'database_connection.php';

header('Content-Type: application/json'); // Set the response type to JSON

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    // Retrieve JSON input
    $input = json_decode(file_get_contents("php://input"), true);

    // Check if JSON decoding succeeded and required fields are present
    if (json_last_error() !== JSON_ERROR_NONE || empty($input['username']) || empty($input['pwd'])) {
        http_response_code(400);  // Bad Request
        echo json_encode(["success" => false, "message" => "Falta usuario o contraseña"]);
        exit;
    }

    $username = htmlspecialchars($input['username']);
    $pwd = htmlspecialchars($input['pwd']);

    // Get database connection
    $conn = getDatabaseConnection();

    if ($conn) {
        // Query to select the user with an exact match for username and password
        $sql = "SELECT * FROM player WHERE user_name = :username AND pwd = :pwd";
        $stmt = $conn->prepare($sql);

        try {
            $stmt->execute(['username' => $username, 'pwd' => $pwd]);
            $result = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($result) {
                // User found; include id_player or default to null if not present
                $id_player = $result['id_player'] ?? null;
                http_response_code(200);  // OK
                echo json_encode(["success" => true, "id_player" => $id_player]);
            } else {
                // User not found
                http_response_code(401);  // Unauthorized
                echo json_encode(["success" => false, "message" => "Usuario o contraseña incorrectos."]);
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
    echo json_encode(["success" => false, "message" => "Credenciales invalidos."]);
}
?>
