import tkinter as tk
from loginView import LoginView
from createAccountView import CreateAccountView
from menuView import MenuView
from gameView import GameView



class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Color blind")
        self.geometry("340x500")
        # Dictionary to store frames
        self.frames = {}

        # Initialize frames
        self.init_frames()

    # Initializing all frames inside the dictionary
    def init_frames(self):
        for F in (LoginView, CreateAccountView, MenuView, GameView):
            page_name = F.__name__
            frame = F(parent=self, controller=self)
            self.frames[page_name] = frame
            frame.grid(row=0, column=0, sticky="nsew")

        # Show the login frame initially
        self.show_frame("LoginView")
    # Navigator will show frames 
    def show_frame(self, page_name):
        
        frame = self.frames[page_name]
        frame.tkraise()

# GUI loop
if __name__ == "__main__":
    app = App()
    app.mainloop()
