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
    def doLogin(connection, username, pwd) -> tuple:
        try:
            if connection is not None:
                cursor = connection.cursor()
                query: str = "SELECT * FROM player WHERE user_name = %s AND pwd = %s"
                params = (username, pwd)  # Use a tuple for parameters
                cursor.execute(query, params)
                # Fetch the result directly from the first cursor
                result = cursor.fetchone()
                return result

        except Exception as e:
            print(f"An error occurred during login: {e}")
            return None
        finally:
            cursor.close()
        
    
    @staticmethod
    def createAccount(connection, username, pwd) -> bool:
        try:
            if connection is not None:
                cursor = connection.cursor()
                # Check if the username already exists
                check_query = "SELECT * FROM player WHERE user_name = %s"
                cursor.execute(check_query, (username,))
                if cursor.fetchone() is not None:
                    return None

                # If the username is available, insert the new account
                insert_query = "INSERT INTO player (user_name, pwd, created_at) VALUES (%s, %s, NOW())"
                cursor.execute(insert_query, (username, pwd))
                connection.commit()  # Commit the transaction
                
                # Check if the insert was successful
                if cursor.rowcount > 0:
                    return True  

        except Exception as e:
            print(f"An error occurred during account creation: {e}")
            return False  
        finally:
            cursor.close()  
        return False  
    
    @staticmethod
    def newGame(connection, id_user, difficulty, isCompleted, score) -> int:
        try:
            if connection is not None:
                cursor = connection.cursor()
                # Check if the username already exists
                check_query = "INSERT INTO game (id_player, difficulty, is_completed, score) VALUES(%s, %s,%s, %s) "
                cursor.execute(check_query, (id_user,difficulty, isCompleted, score))
                connection.commit()
                # Retrieve the ID of the newly inserted row
                cursor.execute("SELECT LAST_INSERT_ID()")
                new_id = cursor.fetchone()[0]  # Fetch the first element which is the new ID
                # Check if the insert was successful
                if cursor.rowcount > 0:
                    return new_id  

        except Exception as e:
            print(f"Error durante creación de juego: {e}")
            return -1
        finally:
            cursor.close()  
        return -1 
    
    @staticmethod
    def gameRecord(connection, id_game,list)-> bool:
        try:
            if connection is not None:
                cursor = connection.cursor()
            # Check if the username already exists
                check_query = "INSERT INTO color_game (id_game, id_color, color, is_succeeded) VALUES(%s, %s,%s, %s) "         
                data_to_insert = [(id_game, id_color, color, is_succeeded) for id_color, color, is_succeeded in list]
                cursor.executemany(check_query, data_to_insert)
                connection.commit()
            # Check if any rows were inserted
            if cursor.rowcount > 0:
                return True
            else:
                return False
        except Exception as e:
            print(f"Error durante creación de juego: {e}")
            return -1
        finally:
            cursor.close()  
    