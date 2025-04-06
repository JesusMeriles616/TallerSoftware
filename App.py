from flask import Flask, request, jsonify
import random

app = Flask(__name__)

# Lista de colores
colours = ['Rojo', 'Azul', 'Verde', 'Rosa', 'Negro', 'Amarillo', 'Naranja', 'Blanco', 'Morado', 'Cafe']
color_map = {
    'Rojo': 'red', 'Azul': 'blue', 'Verde': 'green', 'Rosa': 'pink',
    'Negro': 'black', 'Amarillo': 'yellow', 'Naranja': 'orange',
    'Blanco': 'white', 'Morado': 'purple', 'Cafe': 'brown'
}

# Estado del juego
game_state = {
    "timeleft": 60,
    "score": 0,
    "current_text": "",
    "current_color": ""
}

@app.route('/start_game', methods=['POST'])
def start_game():
    difficulty = request.json.get("difficulty", "Fácil")
    game_state["score"] = 0
    game_state["timeleft"] = 60 if difficulty == "Fácil" else 45 if difficulty == "Medio" else 30
    random.shuffle(colours)
    game_state["current_text"] = colours[0]
    game_state["current_color"] = colours[1]

    return jsonify({
        "message": "Juego iniciado",
        "timeleft": game_state["timeleft"],
        "text": game_state["current_text"],
        "color": game_state["current_color"]
    })

@app.route('/play_game', methods=['POST'])
def play_game():
    if game_state["timeleft"] <= 0:
        return jsonify({"message": "El tiempo se ha acabado.", "score": game_state["score"]})
    
    user_input = request.json.get("color", "").lower()
    correct_color = game_state["current_color"].lower()
    
    if user_input == correct_color:
        game_state["score"] += 1

    random.shuffle(colours)
    game_state["current_text"] = colours[0]
    game_state["current_color"] = colours[1]

    return jsonify({
        "message": "Respuesta recibida",
        "score": game_state["score"],
        "next_text": game_state["current_text"],
        "next_color": game_state["current_color"]
    })

@app.route('/end_game', methods=['POST'])
def end_game():
    final_score = game_state["score"]
    game_state["timeleft"] = 0
    return jsonify({"message": "Juego terminado", "final_score": final_score})

if __name__ == '__main__':
    app.run(debug=True)
