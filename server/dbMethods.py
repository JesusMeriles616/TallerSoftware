import mysql.connector
from mysql.connector import Error

class DataBaseMethod:
    @staticmethod
    def connect():
        try:
            connection = mysql.connector.connect(
                host="localhost",    
                port=41063,            
                user="juan_pablo",
                password="123456789",
                database="color_blind"
            )
            if connection.is_connected():
                print("Successfully connected to the database")
                return connection
        except Error as e:
            print(f"Error connecting to database: {e}")
            return None
    
    @staticmethod
    def disconnect(connection):
        if connection.is_connected():
            connection.close()




    @staticmethod
    def addPlayer(connection, user_name, pwd) -> bool:
        cursor = None  # Initialize cursor
        try:
            if connection is not None:
                cursor = connection.cursor()
                query = "INSERT INTO player (user_name, pwd) VALUES (%s, %s)"
                cursor.execute(query, (user_name, pwd))
                connection.commit()  # Commit the changes to the database
                return True  # Return True to indicate success

        except Exception as e:
            print(f"An error occurred during adding player: {e}")
            return False  # Return False to indicate failure
        finally:
            if cursor is not None:
                cursor.close()  # Close cursor if it was created

    @staticmethod
    def getPlayers(connection) -> tuple:
        try:
            if connection is not None:
                cursor = connection.cursor()
                query: str = "SELECT * FROM player"
                cursor.execute(query)
                # Fetch the result directly from the first cursor
                result = cursor.fetchall()
                return result

        except Exception as e:
            print(f"An error occurred during login: {e}")
            return None
        finally:
            cursor.close()
    @staticmethod
    def deletePlayer(connection, id_player) -> bool:
        try:
            if connection is not None:
                cursor = connection.cursor()
                query : str = "DELETE FROM player where id_player = %s"
                cursor.execute(query, (id_player,))
                connection.commit()
            if cursor.rowcount == 0:
                print("NO SE borro al jugador")
                return False
            else:
                print("SE borro al jugador")
                return True
        
        except mysql.connector.Error as err:
            return f"Error: {err}"
        
        finally:
            cursor.close()
            connection.close()
    @staticmethod
    def updatePlayer(connection, id_player, username, pwd) -> bool:
        try:
            if connection is not None:
                cursor = connection.cursor()
                print("ANTES DE ACTUALIZAR")
                cursor.execute(
                        '''
                    UPDATE player 
                    SET user_name = %s,
                        pwd = %s
                    WHERE id_player = %s
                    ''',
                    (username, pwd, id_player)
                )
                connection.commit()
                
            if cursor.rowcount == 0:
                print("Error al modificar usuario")
                return False
            else:
                print("No se modifico el usuario")
                return True
        
        except mysql.connector.Error as err:
            return f"Error: {err}"
        
        finally:
            cursor.close()
            connection.close()
    @staticmethod
    def getGamesByUser(connection, idUser) -> tuple:
        cursor = None
        try:
            if connection is not None:
                cursor = connection.cursor()
                query: str = """ 
                    SELECT 
                        g.id_game,
                        g.id_player,
                        p.user_name,
                        g.difficulty,
                        g.is_completed,
                        g.score,
                        g.started_at
                    FROM 
                        game g
                    LEFT JOIN 
                        player p 
                    ON 
                        g.id_player = p.id_player
                    WHERE g.id_player = %s;
                """
                print(f"Fetching games for user ID: {idUser}")
                cursor.execute(query, (idUser,))
                result = cursor.fetchall()

                if not result:
                    print("No games found for the specified player ID.")

                return result

        except Exception as e:
            print(f"An error occurred during the query: {e}")
            return None
        finally:
            if cursor:
                cursor.close()
            if connection:
                connection.close()



    @staticmethod
    def getGamesByUserAndDifficulty(connection, idUser,difficulty) -> tuple:
        cursor = None
        try:
            if connection is not None:
                cursor = connection.cursor()
                query: str = """ 
                    SELECT 
                        g.id_game,
                        g.id_player,
                        p.user_name,
                        g.difficulty,
                        g.is_completed,
                        g.score,
                        g.started_at
                    FROM 
                        game g
                    LEFT JOIN 
                        player p 
                    ON 
                        g.id_player = p.id_player
                    WHERE g.id_player = %s 
                    AND g.difficulty = %s;
                """
                print(f"Fetching games for user ID: {idUser}")
                cursor.execute(query, (idUser,difficulty))
                result = cursor.fetchall()

                if not result:
                    print("No games found for the specified player ID.")

                return result

        except Exception as e:
            print(f"An error occurred during the query: {e}")
            return None
        finally:
            if cursor:
                cursor.close()
            if connection:
                connection.close()





    @staticmethod
    def getColorsByGame(connection, id_game) -> tuple:
        try:
            if connection is not None:
                cursor = connection.cursor()
                query: str = """
                    SELECT color, 
                        COUNT(*) AS total_attempts, 
                        SUM(CASE WHEN is_succeeded = 1 THEN 1 ELSE 0 END) AS total_succeeded,
                        SUM(CASE WHEN is_succeeded = 0 THEN 1 ELSE 0 END) AS total_error
                    FROM color_game
                    WHERE id_game = %s
                    GROUP BY color
                    ORDER BY id_color;
                """
                cursor.execute(query, (id_game,))
                result = cursor.fetchall()
                print("ID_GAME:", id_game)
                print("Resultado:", result)
                return result
        except Exception as e:
            print(f"An error occurred while fetching colors for id_game {id_game}: {e}")
            return None
        finally:
            if cursor:
                cursor.close()
