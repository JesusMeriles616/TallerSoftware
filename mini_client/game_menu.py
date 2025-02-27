import utime
import keypad  # Import the keypad module
import power  # Import the power module

def show_game_menu(display):
    """Display the main menu after a successful login."""
    display.fill(0)
    display.text("1. Iniciar", 0, 0)
    display.text("2. Cambiar dificultad", 0, 20)
    display.text("3. Salir", 0, 40)
    display.show()

    # Wait for the user's selection in the main menu
    while True:
        key = keypad.scan_key()
        if key == '1':
            display.fill(0)
            display.text("Iniciar selected", 0, 20)
            display.show()
            utime.sleep(0.5)  # Short delay for message visibility
            show_game_menu(display)  # Return to the main menu after handling the action

        elif key == '2':
            display.fill(0)
            display.text("Cambiar dificultad", 0, 20)
            display.show()
            utime.sleep(0.5)  # Short delay for message visibility
            show_game_menu(display)  # Return to the main menu after handling the action

        elif key == '3':
            display.fill(0)
            display.text("Adios", 0, 20)
            display.show()
            utime.sleep(0.5)  
            break  

