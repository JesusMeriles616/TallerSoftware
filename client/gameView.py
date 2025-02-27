import tkinter as tk
import random
from tkinter import messagebox
from dbMethods import DataBaseMethod
import globals


# List of possible colours.
colours = ['Rojo', 'Azul', 'Verde', 'Rosa', 'Negro', 'Amarillo', 'Naranja', 'Blanco', 'Morado', 'Cafe']

# Dictionary to map Spanish to English color names
color_map = {
    'Rojo': 'red',
    'Azul': 'blue',
    'Verde': 'green',
    'Rosa': 'pink',
    'Negro': 'black',
    'Amarillo': 'yellow',
    'Naranja': 'orange',
    'Blanco': 'white',
    'Morado': 'purple',
    'Cafe': 'brown'
}

keys = list(color_map.keys())


class GameView(tk.Frame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller

        # Initialize score and time left
        self.score = 0
        self.timeleft = 60
        self.game_started = False
        self.stop_game = False
        self.game_color = {}


        # Protocol variables
        self.back_to_login_button = tk.Button(self, text="Logout", command=lambda: controller.show_frame("LoginView"))
        self.back_to_login_button.grid(row=0, column=0, pady=5, padx=5)

        

        # Create a label for difficulty
        self.difficulty_label = tk.Label(self, text=f"Dificultad en: {globals.difficulty}")
        self.difficulty_label.grid(row=1, column=0, pady=10)

        self.difficulty_button = tk.Button(self, text="Dificultad", command=self.open_difficulty_dialog)
        self.difficulty_button.grid(row=2, column=0, pady=5, padx=5)
        
        self.instructions_label = tk.Label(self, text="Ingresa el color que indica el texto",font=('Helvetica', 16))
        self.instructions_label.grid(row = 3,column=0,pady= 10)

        # Add a time left label
        self.timeLabel = tk.Label(self, text="Tiempo restante:  " + str(self.timeleft), font=('Helvetica', 16))
        self.timeLabel.grid(row = 4 ,column=0,pady= 5, padx=5)

        # add a score label
        self.scoreLabel = tk.Label(self, text = "",font = ('Helvetica', 20))
        self.scoreLabel.grid(row = 5, column = 0, pady= 5, padx= 5)

        # Add a label for displaying the colours
        self.label = tk.Label(self, font=('Helvetica', 30))
        self.label.grid(row = 6,column=0,pady= 5)

        # Add a text entry box for typing in colours
        self.input_field = tk.Entry(self)
        self.input_field.grid(row = 7,column=0,pady= 10)
        self.input_field.bind('<Return>', self.playGame)


        self.initGame = tk.Button(self, text="Iniciar",  command=self.startGame)
        self.initGame.grid(row=8, column=0, pady=5, padx=5)

         # Create "Reset" button (hidden initially)
        self.reset_button = tk.Button(self, text="Abandonar", command=self.resetGame)
        self.reset_button.grid(row=9, column=0, pady=5, padx=5)
        self.reset_button.grid_remove()  # Hide the button initially

        # Set focus on the entry box
        self.input_field.focus_set()

    def update_timeleft_label(self):
    # Update the time left label
        self.timeLabel.config(text="Tiempo restante: " + str(self.timeleft))

    def open_difficulty_dialog(self):
        dialog = tk.Toplevel(self)
        dialog.title(f"Seleccionar Dificultad")

        tk.Label(dialog, text="Elige una dificultad:").pack(pady=10)

        difficulties = ["Fácil", "Medio", "Difícil"]
        for difficulty in difficulties:
            button = tk.Button(dialog, text=difficulty, command=lambda d=difficulty: self.set_difficulty(d, dialog))
            button.pack(pady=5)

    def set_difficulty(self, difficulty, dialog):
        match difficulty:
            case "Fácil":
                globals.difficulty = "Fácil"
                self.timeleft = 60
            case "Medio":
                globals.difficulty = "Medio"
                self.timeleft = 45
            case "Difícil":
                globals.difficulty = "Difícil"
                self.timeleft = 30
        self.update_difficulty_label()
        self.update_timeleft_label()
        messagebox.showinfo("Dificultad Seleccionada", f"Has seleccionado: {difficulty}")
        dialog.destroy()


    def update_difficulty_label(self):
         # Update the displayed difficulty
        self.difficulty_label.config(text=f"Dificultad en: {globals.difficulty}")
    
    # Method to start the game
    def startGame(self, event=None):
        self.timeleft = 30 if globals.difficulty == "Difícil" else 45 if globals.difficulty == "Medio" else 60 if globals.difficulty == "Fácil" else 90
        self.score = 0
        self.game_started = True
        self.stop_game = False  # Reset the stop_game flag
        self.scoreLabel.config(text="Puntaje: " + str(self.score))
        self.update_timeleft_label()

        # Disable the "Logout" and "Dificultad" buttons
        self.back_to_login_button.config(state="disabled")
        self.difficulty_button.config(state="disabled")
        self.initGame.config(state="disabled")
        self.reset_button.grid()

        # Start the countdown timer
        self.countdown()

        # Start showing colors for the game
        self.nextColour()

    # Method to play the game xd
    def playGame(self, event=None):
        if not self.game_started:
            return  # Do nothing if the game hasn't started

        if self.timeleft > 0:
            flag = 0
            index = keys.index(colours[1]) + 1
            color = colours[1].upper()
            # Make the text entry box active
            self.input_field.focus_set()

            # Check if the typed color matches the displayed color
            if self.input_field.get().lower() == colours[1].lower():
                self.score += 1
                flag = 1

            # Clear the text entry box
            self.input_field.delete(0, tk.END)

            # Add the color and flag to the globals.game_color dictionary
            globals.game_color.append((index,color,flag))
            # Call the next color method
            self.nextColour()

    def nextColour(self):
        # If a game is currently in play
        if self.timeleft > 0:
            # Make the text entry box active
            self.input_field.focus_set()

            # Check if the typed colour matches the displayed colour
            # if self.input_field.get().lower() == colours[1].lower():
            #     self.score += 1

            # Clear the text entry box
            self.input_field.delete(0, tk.END)

            # Shuffle colours and update the label
            random.shuffle(colours)
            # print(colours[1].lower())
          
            # print(index)


            # Set the foreground color using the English equivalent
            self.label.config(fg=color_map[colours[1]], text=str(colours[0]))
           
            # Update the score
            self.scoreLabel.config(text="Puntaje: " + str(self.score))
           

    def countdown(self):
        if self.stop_game:
            return  # Stop the countdown if the game is abandoned or reset.

        if self.timeleft > 0:
            self.timeleft -= 1
            self.timeLabel.config(text="Tiempo restante: " + str(self.timeleft))
            self.after(1000, self.countdown)
        else:
            flag = self.sendNewGame(1)
            if flag is False:
                messagebox.OK("Fallo de conexión", "Problemas con la base de datos")
                self.restartGame()
                return
            flag = self.sendColorData()
            if flag is False:
                messagebox.OK("Fallo de conexión", "Problemas con la base de datos")
                self.restartGame()
                return
            result = messagebox.askyesno("Fin del juego", "¿Quieres jugar de nuevo?")
            if result:
                self.startGame()
            else:
                self.endGame()




    def restartGame(self):
        # Reset the timer and score, and restart the game
        self.timeleft = 30
        self.score = 0
        self.scoreLabel.config(text="Puntaje: " + str(self.score))
        self.timeLabel.config(text="Tiempo restante: " + str(self.timeleft))
        self.resetUI()

    def endGame(self):
        # End the game (you can return to the menu or close the game window)
        self.back_to_login_button.config(state="normal")
        self.difficulty_button.config(state="normal")
        self.initGame.config(state="normal")
        self.reset_button.grid_remove()
        self.resetUI()
        self.controller.show_frame("LoginView")


    def resetGame(self):
        self.stop_game = True  # Set the flag to stop the game
        self.sendNewGame(0)
        self.sendColorData()
        self.resetUI()
    
    def resetUI(self):
        self.game_started = False
        self.timeleft = 30 if globals.difficulty == "Difícil" else 45 if globals.difficulty == "Medio" else 60
        self.score = 0
        self.input_field.delete(0, tk.END)
        self.label.config(text="")
        self.scoreLabel.config(text="Puntaje: " + str(self.score))
        self.timeLabel.config(text="Tiempo restante: " + str(self.timeleft))
        self.update_difficulty_label()
        
        # Re-enable buttons
        self.back_to_login_button.config(state="normal")
        self.difficulty_button.config(state="normal")
        self.initGame.config(state="normal")
        self.reset_button.grid_remove()



    def sendNewGame(self, isCompleted):
        connection = DataBaseMethod.connect()
        if connection is None:
            messagebox.showerror("Problemas de conexión", "Fallo en conectar a la Base de datos.")
            return
        globals.id_game = DataBaseMethod.newGame(connection=connection, id_user=globals.id_user,difficulty=globals.difficulty, isCompleted=isCompleted, score=self.score)
        if globals.id_game != -1:
            return True
        else:
            globals.id_game = -1
            return False
        
    def sendColorData(self):
        connection = DataBaseMethod.connect()
        if connection is None:
            messagebox.showerror("Problemas de conexión", "Fallo en conectar a la Base de datos.")
            return
        return DataBaseMethod.gameRecord(connection=connection, id_game=globals.id_game,list=globals.game_color)


   