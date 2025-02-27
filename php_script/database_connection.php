<?php 

function getDatabaseConnection() {
    $host = "192.168.1.101"; // Replace with the ip of the server 
    $port = "41063";
    $username = "juan_pablo";
    $password = "1234";
    $dbname = "color_blind";

    try {
        $conn = new PDO("mysql:host=$host;port=$port;dbname=$dbname", $username, $password);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        echo "Database connection successful.";
        return $conn;

    } catch (PDOException $e) {
        echo "Database connection failed: " . $e->getMessage();
        return null;
    }
}

?>