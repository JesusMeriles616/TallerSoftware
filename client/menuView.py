import tkinter as tk
from tkinter import messagebox
import globals

class MenuView(tk.Frame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller

        

        self.welcome_label =  tk.Label(self, text=f"¡Bienvenido {globals.userName}!")
        self.welcome_label.grid(row=0, column=0, pady=20)


         # Create a label for difficulty
        self.difficulty_label = tk.Label(self, text=f"Dificultad en: {globals.difficulty}")
        self.difficulty_label.grid(row=0, column=2, pady=20)

        back_to_login_button = tk.Button(self, text="Logout", command=lambda: controller.show_frame("LoginView"))
        back_to_login_button.grid(row=1, column=0, pady=10, padx=10)

        initGame = tk.Button(self, text="Iniciar",  command=lambda: controller.show_frame("GameView"))
        initGame.grid(row=1, column=1, pady=10, padx=10)

        difficulty = tk.Button(self, text="Dificultad", command=self.open_difficulty_dialog)
        difficulty.grid(row=1, column=2, pady=10, padx=10)
    

    
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

            case "Medio":
                globals.difficulty = "Medio"
      
            case "Difícil":
                globals.difficulty= "Difícil"

        # Update the difficulty label
        self.update_difficulty_label()
        messagebox.showinfo("Dificultad Seleccionada", f"Has seleccionado: {difficulty}")
        dialog.destroy()


    def update_difficulty_label(self):
         # Update the displayed difficulty
        self.difficulty_label.config(text=f"Dificultad en: {globals.difficulty}")

    def update_welcome_label(self):
        self.welcome_label.config(text=f"¡Bienvenido {globals.userName}!")