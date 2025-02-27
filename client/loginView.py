import tkinter as tk
from tkinter import messagebox
from dbMethods import DataBaseMethod
import globals

class LoginView(tk.Frame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller # Reference to the App (main) instance

        
        tk.Label(self, text="Login", font=('Helvetica', 16)).grid(row=0, column=3, pady=10)

        tk.Label(self, text="Usuario").grid(row=1, column=2)
        self.username_entry = tk.Entry(self)
        self.username_entry.grid(row=1, column=3)

        tk.Label(self, text="Contraseña").grid(row=2, column=2)
        self.password_entry = tk.Entry(self, show="*")
        self.password_entry.grid(row=2, column=3)

        login_button = tk.Button(self, text="Login", command=self.login)
        login_button.grid(row=3, column=3, pady=10)

        create_account_button = tk.Button(self, text="Crear cuenta", command=lambda: controller.show_frame("CreateAccountView"))
        create_account_button.grid(row=4, column=3, pady=10)

    def login(self):
        connection = DataBaseMethod.connect()
        if connection is None:
            messagebox.showerror("Problemas de conexión", "Fallo en conectar a la Base de datos.")
            return
        username = self.username_entry.get()
        password = self.password_entry.get()
        if username == "":
            messagebox.showerror("Usuario vacio", "Llene todos los campos del formulario")
            return
        if password == "":
            messagebox.showerror("Contraseña vacia", "Llene todos los campos del formulario")
            return
        ans = DataBaseMethod.doLogin(connection=connection, username=username, pwd=password)            
        if ans is None:
            messagebox.showerror("Intento fallido", "Credenciales incorrectos")
            return
        else:
            messagebox.showinfo("Bienvenido", f"Disfrute del juego \"{ans[1]}\"")
            # Changing Global variables
            globals.isLogedIn = True
            globals.userName = username
            globals.id_user = ans[0]
            globals.difficulty = "Fácil"
            globals.game_color = []
            DataBaseMethod.disconnect(connection)
            game_view = self.controller.frames["GameView"]
            game_view.update_difficulty_label()
            # changing view
            self.controller.show_frame("GameView")
    

    # Method to clear the Entry fields
    def clear_entries(self):
        self.username_entry.delete(0, tk.END)
        self.password_entry.delete(0, tk.END)

    # Override the method to raise the frame
    def tkraise(self):
        self.clear_entries()  
        super().tkraise()