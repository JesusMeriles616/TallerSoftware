from flask import Blueprint, jsonify, render_template, redirect, request, url_for
from dbMethods import DataBaseMethod

# Create a blueprint for jugadores
color_bp = Blueprint('color_bp', __name__)

# Route to list players
@color_bp.route('/color')
def jugadores():
    id_game = request.args.get('id_game', type=int)  # Get the id_game parameter    
    return render_template('color.html', jugadores=jugadores, id_game=id_game)


@color_bp.route('/color_bar_graph/<int:id_game>')
def bar_graph(id_game):
    connection = DataBaseMethod.connect()
    colors_data = DataBaseMethod.getColorsByGame(connection, id_game)
    print(colors_data)
    if colors_data:
        colors, total_attempts, total_succeeded, total_error = zip(*colors_data)

        bar_data = [
            {
                'x': list(colors),
                'y': list(total_attempts),
                'name': 'Intentos totales',
                'type': 'bar'
            },
            {
                'x': list(colors),
                'y': list(total_succeeded),
                'name': 'Exitosos',
                'type': 'bar'
            },
            {
                'x': list(colors),
                'y': list(total_error),
                'name': 'Errores',
                'type': 'bar'
            }
        ]

        layout = {
            'barmode': 'group',
            'title': 'Intentos por color',
            'xaxis': {
                'title': 'Colores'
            },
            'yaxis': {
                'title': 'Cantidad intentos'
            }
        }

        return jsonify(data=bar_data, layout=layout)
    else:
        return jsonify({'error': 'No data available for this game'}), 404
