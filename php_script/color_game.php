<?php 
require 'database_connection.php';

header('Content-Type: application/json'); // Set the response type to JSON

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    // Retrieve JSON input
    $input = json_decode(file_get_contents("php://input"), true);

    // Check if JSON decoding succeeded and required fields are present
    if (json_last_error() !== JSON_ERROR_NONE || empty($input['id_game']) || empty($input['id_color_true']) || empty($input['id_color_pressed']) || !isset($input['is_succeeded']) || empty($input['order_is'])) {
        http_response_code(400);  // Bad Request
        echo json_encode(["success" => false, "message" => "Falta datos entrantes"]);
        exit;
    }

    $id_game = htmlspecialchars($input['id_game']); 
    $id_color_true = htmlspecialchars($input['id_color_true']);
    $id_color_pressed = htmlspecialchars($input['id_color_pressed']);
    $is_succeeded = (int) $input['is_succeeded'];  // 0 or 1
    $order_is = (int) $input['order_is'];

    // Get database connection
    $conn = getDatabaseConnection();

    if ($conn) {
        // SQL query to insert the color game data
        $sql = "INSERT INTO color_game (id_game, id_color_true, id_color_pressed, is_succeeded, order_is) VALUES (:id_game, :id_color_true, :id_color_pressed, :is_succeeded, :order_is)";
        $stmt = $conn->prepare($sql);

        try {
            // Execute the statement with the provided data
            $stmt->execute([
                'id_game' => $id_game, 
                'id_color_true' => $id_color_true, 
                'id_color_pressed' => $id_color_pressed, 
                'is_succeeded' => $is_succeeded, 
                'order_is' => $order_is
            ]);
            http_response_code(201);  // Created
            echo json_encode(["success" => true, "message" => "Color game data inserted successfully."]);
        } catch (PDOException $e) {
            // SQL execution error
            http_response_code(500);  // Internal Server Error
            echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
        }
    } else {
        // Database connection error
        http_response_code(500);  // Internal Server Error
        echo json_encode(["success" => false, "message" => "Database connection failed."]);
    }
} else {
    // Invalid request method
    http_response_code(405);  // Method Not Allowed
    echo json_encode(["success" => false, "message" => "Method not allowed."]);
}
?>
