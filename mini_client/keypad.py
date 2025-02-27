from machine import Pin
import utime

# Keypad configuration
matrix_keys = [['1', '2', '3', 'A'],
               ['4', '5', '6', 'B'],
               ['7', '8', '9', 'C'],
               ['*', '0', '#', 'D']]

keypad_rows = [9, 8, 7, 6]  # Adjust these pins to match your wiring
keypad_columns = [5, 4, 3, 2]  # Adjust these pins to match your wiring

# Setting up row and column pins
col_pins = []
row_pins = []

def init_keypad():
    """Initialize the row and column pins for the keypad."""
    global col_pins, row_pins
    for x in range(4):
        row_pins.append(Pin(keypad_rows[x], Pin.OUT))
        row_pins[x].value(1)
        col_pins.append(Pin(keypad_columns[x], Pin.IN, Pin.PULL_DOWN))

def scan_key():
    """Scan the keypad and return the pressed key."""
    for row in range(4):
        row_pins[row].high()
        for col in range(4):
            if col_pins[col].value() == 1:
                utime.sleep(0.3)  # Debounce delay
                row_pins[row].low()
                return matrix_keys[row][col]
        row_pins[row].low()
    return None

