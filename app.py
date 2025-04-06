from flask import Flask, request, jsonify
from flasgger import Swagger, swag_from

app = Flask(__name__)

app.config['SWAGGER'] = {
    'title': 'API de Juego de Colores',
    'uiversion': 3,
    'description': 'API para manejar un juego simple de adivinanza de colores'
}
swagger = Swagger(app)

games = {}
game_id = 1

def validar_game_id(game_id_str):
    """
    Valida que el game_id sea un entero mayor o igual a cero.
    Devuelve el game_id como entero si es válido, None si no lo es.
    """
    try:
        game_id = int(game_id_str)
        if game_id >= 0:
            return game_id
        return None
    except (ValueError, TypeError):
        return None

@app.route('/start_game', methods=['POST'])
@swag_from({
    'tags': ['Game'],
    'description': 'Inicia un nuevo juego con la dificultad especificada',
    'parameters': [
        {
            'name': 'body',
            'in': 'body',
            'required': True,
            'schema': {
                'type': 'object',
                'properties': {
                    'difficulty': {
                        'type': 'string',
                        'enum': ['Fácil', 'Medio', 'Difícil'],
                        'default': 'Fácil',
                        'description': 'Nivel de dificultad del juego'
                    }
                }
            }
        }
    ],
    'responses': {
        200: {
            'description': 'Juego iniciado correctamente',
            'examples': {
                'application/json': {
                    'game_id': 1,
                    'timeleft': 60,
                    'difficulty': 'Fácil'
                }
            }
        }
    }
})
def start_game():
    global game_id
    difficulty = request.json.get("difficulty", "Fácil")
    timeleft = 60 if difficulty == "Fácil" else 45 if difficulty == "Medio" else 30
    games[game_id] = {"score": 0, "timeleft": timeleft, "difficulty": difficulty}
    response = {"game_id": game_id, "timeleft": timeleft, "difficulty": difficulty}
    game_id += 1
    return jsonify(response)

@app.route('/play', methods=['POST'])
@swag_from({
    'tags': ['Game'],
    'description': 'Realiza un movimiento en el juego adivinando el color',
    'parameters': [
        {
            'name': 'body',
            'in': 'body',
            'required': True,
            'schema': {
                'type': 'object',
                'properties': {
                    'game_id': {
                        'type': 'integer',
                        'description': 'ID del juego en curso'
                    },
                    'color': {
                        'type': 'string',
                        'description': 'Color que el usuario cree que es el correcto'
                    }
                },
                'required': ['game_id', 'color']
            }
        }
    ],
    'responses': {
        200: {
            'description': 'Respuesta después de jugar',
            'examples': {
                'application/json': {
                    'score': 1,
                    'timeleft': 60
                }
            }
        },
        400: {
            'description': 'Game ID inválido',
            'examples': {
                'application/json': {
                    'error': 'Game ID must be a positive integer'
                }
            }
        },
        404: {
            'description': 'Game ID no encontrado',
            'examples': {
                'application/json': {
                    'error': 'Game ID not found'
                }
            }
        }
    }
})
def play():
    game_id_input = request.json.get("game_id")
    user_input = request.json.get("color")
    
    # Validar game_id
    game_id = validar_game_id(game_id_input)
    if game_id is None:
        return jsonify({"error": "Game ID must be a positive integer"}), 400
    
    if game_id not in games:
        return jsonify({"error": "Game ID not found"}), 404
    
    correct_color = "Rojo"  
    if user_input.lower() == correct_color.lower():
        games[game_id]["score"] += 1

    return jsonify({"score": games[game_id]["score"], "timeleft": games[game_id]["timeleft"]})

@app.route('/end_game', methods=['POST'])
@swag_from({
    'tags': ['Game'],
    'description': 'Finaliza un juego en curso y obtiene el puntaje final',
    'parameters': [
        {
            'name': 'body',
            'in': 'body',
            'required': True,
            'schema': {
                'type': 'object',
                'properties': {
                    'game_id': {
                        'type': 'integer',
                        'description': 'ID del juego a finalizar'
                    }
                },
                'required': ['game_id']
            }
        }
    ],
    'responses': {
        200: {
            'description': 'Juego finalizado correctamente',
            'examples': {
                'application/json': {
                    'message': 'Game ended',
                    'final_score': 1
                }
            }
        },
        400: {
            'description': 'Game ID inválido',
            'examples': {
                'application/json': {
                    'error': 'Game ID must be a positive integer'
                }
            }
        },
        404: {
            'description': 'Game ID no encontrado',
            'examples': {
                'application/json': {
                    'error': 'Game ID not found'
                }
            }
        }
    }
})
def end_game():
    game_id_input = request.json.get("game_id")
    
    # Validar game_id
    game_id = validar_game_id(game_id_input)
    if game_id is None:
        return jsonify({"error": "Game ID must be a positive integer"}), 400
    
    if game_id in games:
        final_score = games.pop(game_id)["score"]
        return jsonify({"message": "Game ended", "final_score": final_score})
    return jsonify({"error": "Game ID not found"}), 404

if __name__ == '__main__':
    app.run(debug=True)