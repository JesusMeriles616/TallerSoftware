import tkinter as tk
from tkinter import messagebox
from dbMethods import DataBaseMethod

class CreateAccountView(tk.Frame):
    def __init__(self, parent, controller):
        super().__init__(parent)
        self.controller = controller

        tk.Label(self, text="Creación de cuenta").grid(row=0, column=1, pady=10)

        tk.Label(self, text="Usuario").grid(row=1, column=0)
        self.username_entry = tk.Entry(self)
        self.username_entry.grid(row=1, column=1)

        tk.Label(self, text="Contraseña").grid(row=2, column=0)
        self.password_entry = tk.Entry(self, show="*")
        self.password_entry.grid(row=2, column=1)

        tk.Label(self, text="Confirme contraseña").grid(row=3, column=0)
        self.confirm_password_entry = tk.Entry(self, show="*")
        self.confirm_password_entry.grid(row=3, column=1)

        create_button = tk.Button(self, text="Crear cuenta", command=self.create_account)
        create_button.grid(row=4, column=1, pady=10)

        back_button = tk.Button(self, text="Volver a login", command=lambda: controller.show_frame("LoginView"))
        back_button.grid(row=5, column=1)

    def create_account(self):
        connection = DataBaseMethod.connect()
        if connection is None:
            messagebox.showerror("Problemas de conexión", "Fallo en conectar a la Base de datos.")
            return

        # Simple password match validation (replace with real logic)
        username = self.username_entry.get()
        password = self.password_entry.get()
        confirm_password = self.confirm_password_entry.get()

        if username == "":
            messagebox.showerror("Usuario vacio", "Llene todos los campos del formulario")
            return
        if password == "":
            messagebox.showerror("Contraseña vacia", "Llene todos los campos del formulario")
            return
        if confirm_password == "":
            messagebox.showerror("Confirmar contraseña vacia", "Llene todos los campos del formulario")
            return
        if password != confirm_password:
            messagebox.showerror("Error", "Contraseñas no coinciden")
            
            return
        ans = DataBaseMethod.createAccount(connection=connection,username=username,pwd=password)
        if ans is True:
            messagebox.showinfo("Cuenta creada", f"¡Cuenta para \"{username}\" creada!")
            self.controller.show_frame("LoginView")
        DataBaseMethod.disconnect(connection)


    # Method to clear the Entry fields
    def clear_entries(self):
        self.username_entry.delete(0, tk.END)
        self.password_entry.delete(0, tk.END)
        self.confirm_password_entry.delete(0, tk.END)

    # Override the method to raise the frame
    def tkraise(self):
        self.clear_entries()  
        super().tkraise()
