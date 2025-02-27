from machine import I2C, Pin
import utime
from lib import sh1106  # Adjust path as necessary
import keypad  # Import the keypad module

# Display setup (assuming 128x64 resolution)
i2c = I2C(0, scl=Pin(17), sda=Pin(16), freq=400000) 
display = sh1106.SH1106_I2C(128, 64, i2c)

def show_login_menu():
    """Display the login menu."""
    display.fill(0)  # Clear display
    display.text("Color-blind", 0, 0)       # Title
    display.text("1. Ingresar", 0, 20)      # Option 1
    display.text("2. Salir", 0, 40)         # Option 2
    display.show()

def input_credentials():
    """Prompt for username and password input via keypad."""
    username = ""
    password = ""
    input_mode = "username"  # Start with username input
    
    display.fill(0)
    display.text("Ingrese usuario:", 0, 0)
    display.show()
    
    while True:
        key = keypad.scan_key()  # Use the keypad to scan for key presses
        
        if key:
            if key == '#':  # Confirm input or switch modes
                if input_mode == "username":
                    input_mode = "password"
                    display.fill(0)
                    display.text("Ingrese password:", 0, 0)
                    display.show()
                    utime.sleep(0.5)
                elif input_mode == "password":
                    break  # Finish input
            elif key == '*':  # Backspace functionality
                if input_mode == "username" and len(username) > 0:
                    username = username[:-1]
                    display.fill(0)
                    display.text("Ingrese usuario:", 0, 0)
                    display.text("Username: " + username, 0, 20)
                elif input_mode == "password" and len(password) > 0:
                    password = password[:-1]
                    display.fill(0)
                    display.text("Ingrese password:", 0, 0)
                    display.text("Password: " + "*" * len(password), 0, 20)  # Mask password
                display.show()  # Update the display to reflect deletion
            else:
                # Add the key to the appropriate input
                if input_mode == "username":
                    username += key
                    display.fill(0)
                    display.text("Ingrese usuario:", 0, 0)
                    display.text("Username: " + username, 0, 20)
                elif input_mode == "password":
                    password += key
                    display.fill(0)
                    display.text("Ingrese password:", 0, 0)
                    display.text("Password: " + "*" * len(password), 0, 20)  # Mask password
                display.show()
                utime.sleep(0.3)  # Debounce delay

    # Display username and masked password for confirmation
    display.fill(0)
    display.text("Username: " + username, 0, 20)
    display.text("Password: " + "*" * len(password), 0, 40)
    display.show()
    utime.sleep(2)
    return username, password


