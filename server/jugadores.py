from flask import Blueprint, render_template, redirect, request, url_for
from dbMethods import DataBaseMethod

# Create a blueprint for jugadores
jugadores_bp = Blueprint('jugadores_bp', __name__)

# Route to list players
@jugadores_bp.route('/jugadores')
def jugadores():
    connection = DataBaseMethod.connect()
    jugadores = DataBaseMethod.getPlayers(connection=connection)
    return render_template('jugadores.html', jugadores=jugadores)

@jugadores_bp.route('/jugadores/delete_player/<int:player_id>', methods=['POST'])
def delete_player(player_id):
    connection = DataBaseMethod.connect()
    result = DataBaseMethod.deletePlayer(connection=connection, id_player=player_id)
    if result:
        return redirect(url_for('jugadores_bp.jugadores'))
    else:
        return "Error deleting player", 500
    

@jugadores_bp.route('/jugadores/update_player', methods=['POST'])
def update_player():
    try:
        id_player = request.form['updatePlayerId']
        username = request.form['playerName']
        pwd = request.form['pwd']
        connection = DataBaseMethod.connect()
        result = DataBaseMethod.updatePlayer(connection=connection, id_player=id_player, username=username, pwd=pwd)
        if result:
            return redirect(url_for('jugadores_bp.jugadores'))
        else:
            return "Error al modificar usuario", 500
    except Exception as e:
        print(f"Error: {e}")
        return "Error al modificar usuario", 500
    

@jugadores_bp.route('/jugadores/add_player', methods=['POST'])
def add_player():
    try:
        username = request.form['playerName']
        pwd = request.form['pwd']
        connection = DataBaseMethod.connect()
        result = DataBaseMethod.addPlayer(connection=connection, user_name=username, pwd=pwd)
        if result:
            return redirect(url_for('jugadores_bp.jugadores'))
        else:
            return "Error al modificar usuario", 500
    except Exception as e:
        print(f"Error: {e}")
        return "Error al modificar usuario", 500
  