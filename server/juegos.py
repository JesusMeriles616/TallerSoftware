import datetime
from typing import Counter
from flask import Blueprint, json, jsonify, render_template, redirect, request, url_for
from dbMethods import DataBaseMethod
import plotly.graph_objs as go
import plotly.io as pio

# Create a blueprint for jugadores
juegos_bp = Blueprint('juegos_bp', __name__)

@juegos_bp.route('/juegos/<int:id_player>')
def juegos(id_player):
    return render_template('juegos.html', id_player=id_player)



# Ruta para actualizar los datos del gráfico
# Route to update the graph data
@juegos_bp.route('/datos_grafico', methods=['GET'])
def datos_pie_grafico():
    # Obtain the player ID from the request
    id_player = request.args.get('id_player', type=int)

    # Ensure the player ID is valid
    if id_player is None:
        return jsonify({'error': 'ID de jugador no proporcionado'}), 400

    # Get game data for the specified player
    connection = DataBaseMethod.connect()
    games = DataBaseMethod.getGamesByUser(connection=connection, idUser=id_player)

    if not games:
        return jsonify({'error': 'No se encontraron juegos para el jugador'}), 404

    # Get the player's name from the games data (assuming it's at index 2)
    player_name = games[0][2] if games else "Jugador Desconocido"

    # Count completed and incomplete games
    completed_games = sum(1 for game in games if game[4] == 1)  # Assuming is_completed is the 4th element (1 for completed)
    incomplete_games = sum(1 for game in games if game[4] == 0)  # Assuming 0 for not completed

    # Calculate total number of games
    total_games = len(games)

    # Prepare data for pie chart
    labels = ['Completados', 'Incompletos']
    values = [completed_games, incomplete_games]

    # Create the Plotly pie chart
    pie_trace = go.Pie(labels=labels, values=values, name='Juegos')

    # Update the layout to include the player's name and total games in the title
    layout = go.Layout(
        title=f'Distribución de Juegos Completados e Incompletos para {player_name}',
        margin=dict(l=40, r=40, t=40, b=100),  # Increased bottom margin to fit labels
        height=350,  # Decreased the height of the pie chart
        annotations=[
            {
                'text': f'Total Juegos: {total_games}',  # Display total number of games
                'x': 0.5, 'y': -0.1,  # Position below the pie chart
                'xref': 'paper', 'yref': 'paper',
                'showarrow': False,
                'font': dict(size=12, color="black")
            },
            {
                'text': f'Completados: {completed_games}',  # Display completed games
                'x': 0.5, 'y': -0.15,  # Position below the total games
                'xref': 'paper', 'yref': 'paper',
                'showarrow': False,
                'font': dict(size=12, color="green")
            },
            {
                'text': f'Incompletos: {incomplete_games}',  # Display incomplete games
                'x': 0.5, 'y': -0.2,  # Position below completed games
                'xref': 'paper', 'yref': 'paper',
                'showarrow': False,
                'font': dict(size=12, color="red")
            }
        ]
    )

    # Create the figure with the pie chart and layout
    fig = go.Figure(data=[pie_trace], layout=layout)

    # Convert the figure to JSON
    pie_graph_json = pio.to_json(fig)

    return jsonify(pie_graph_json)




@juegos_bp.route('/datos_grafico_dificultad', methods=['GET'])
def datos_pie_dificultad():
    # Obtain the player ID and difficulty from the request
    id_player = request.args.get('id_player', type=int)
    difficulty = request.args.get('difficulty', type=str)

    # Ensure the player ID is valid
    if id_player is None:
        return jsonify({'error': 'ID de jugador no proporcionado'}), 400

    # Get game data for the specified player and difficulty
    connection = DataBaseMethod.connect()
    games = DataBaseMethod.getGamesByUserAndDifficulty(connection=connection, idUser=id_player, difficulty=difficulty)

    if not games:
        return jsonify({'error': 'No se encontraron juegos para el jugador'}), 404

    # Get the player's name from the games data (assuming it's at index 2)
    player_name = games[0][2] if games else "Jugador Desconocido"

    # Count completed and incomplete games
    completed_games = sum(1 for game in games if game[4] == 1)  # Assuming is_completed is the 4th element (1 for completed)
    incomplete_games = sum(1 for game in games if game[4] == 0)  # Assuming 0 for not completed

    # Calculate total number of games
    total_games = len(games)

    # Prepare data for pie chart
    labels = ['Completados', 'Incompletos']
    values = [completed_games, incomplete_games]

    # Create the Plotly pie chart
    pie_trace = go.Pie(labels=labels, values=values, name='Juegos')

    # Update the layout to include the player's name, difficulty, and total games in the title
    layout = go.Layout(
        title=f'Juegos por dificultad {difficulty} para {player_name}',
        margin=dict(l=40, r=40, t=40, b=100),  # Increased bottom margin to fit labels
        height=350,  # Reduced the height of the pie chart
        annotations=[
            {
                'text': f'Total Juegos: {total_games}',  # Display total number of games
                'x': 0.5, 'y': -0.1,  # Position below the pie chart
                'xref': 'paper', 'yref': 'paper',
                'showarrow': False,
                'font': dict(size=12, color="black")
            },
            {
                'text': f'Completados: {completed_games}',  # Display completed games
                'x': 0.5, 'y': -0.15,  # Position below the total games
                'xref': 'paper', 'yref': 'paper',
                'showarrow': False,
                'font': dict(size=12, color="green")
            },
            {
                'text': f'Incompletos: {incomplete_games}',  # Display incomplete games
                'x': 0.5, 'y': -0.2,  # Position below completed games
                'xref': 'paper', 'yref': 'paper',
                'showarrow': False,
                'font': dict(size=12, color="red")
            }
        ]
    )

    # Create the figure with the pie chart and layout
    fig = go.Figure(data=[pie_trace], layout=layout)

    # Convert the figure to JSON
    pie_graph_json = pio.to_json(fig)

    return jsonify(pie_graph_json)




@juegos_bp.route('/datos_grafico_scatter', methods=['GET'])
def datos_grafico_scatter():
    # Obtain the player ID from the request
    id_player = request.args.get('id_player', type=int)

    # Ensure the player ID is valid
    if id_player is None:
        return jsonify({'error': 'ID de jugador no proporcionado'}), 400

    # Get game data for the specified player
    connection = DataBaseMethod.connect()
    games = DataBaseMethod.getGamesByUser(connection=connection, idUser=id_player)
    player_name = games[0][2] if games else "Jugador Desconocido"

    if not games:
        return jsonify({'error': 'No se encontraron juegos para el jugador'}), 404

   # Prepare data for the bar chart
    
  

    # Separate the data into completed and not completed
    completed_games = [(game[6], game[5], game[3]) for game in games if game[4] == 1]  # Completed games
    not_completed_games = [(game[6], game[5], game[3]) for game in games if game[4] == 0]  # Not completed games

    # Create the Plotly bar chart for completed games (green) with difficulty tags
    completed_trace = go.Bar(
        x=[game[0] for game in completed_games],
        y=[game[1] for game in completed_games],
        text=[game[2] for game in completed_games],  # Difficulty labels
        textposition='auto',  # Automatically position the text
        marker=dict(color='green', opacity=0.7),
        name='Completado'  # Legend label
    )

    # Create the Plotly bar chart for not completed games (red) with difficulty tags
    not_completed_trace = go.Bar(
        x=[game[0] for game in not_completed_games],
        y=[game[1] for game in not_completed_games],
        text=[game[2] for game in not_completed_games],  # Difficulty labels
        textposition='auto',  # Automatically position the text
        marker=dict(color='red', opacity=0.7),
        name='Sin completar'  # Legend label
    )

       # Prepare data for the scatter plot
    scatter_x = [game[6] for game in games]  # `started_at` is at index 6
    scatter_y = [game[5] for game in games]  # `score` is at index 5
    difficulties = [game[3] for game in games]  # `difficulty` is at index 3
    game_ids = [game[0] for game in games]  # `id_game` is at index 0

        # Create the Plotly scatter plot
    scatter_trace = go.Scatter(
        x=scatter_x,
        y=scatter_y,
        mode='markers',
        text=difficulties,  # Use this to display difficulty on hover
        customdata=game_ids,  # Include game IDs as custom data
        marker=dict(size=10),
        name='Puntuaciones'  # Name for the scatter trace
    )


    # Update the layout for the bar chart with legend
    layout = go.Layout(
        title=f'Puntuaciones de Juegos para Jugador {player_name}',
        xaxis=dict(title='Fecha de Inicio'),
        yaxis=dict(title='Puntuación'),
        margin=dict(l=40, r=40, t=40, b=40),
        legend=dict(title="", orientation="h", x=0.5, xanchor="center", y=1.1)  # Legend settings
    )

# Create the figure with both traces
    fig = go.Figure(data=[completed_trace, not_completed_trace, scatter_trace], layout=layout)

    # Convert the figure to JSON
    bar_graph_json = pio.to_json(fig)

    return jsonify(bar_graph_json)


