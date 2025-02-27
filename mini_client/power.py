from machine import I2C, Pin, deepsleep  # Import I2C and Pin
import utime
from lib import sh1106  # Adjust path as necessary

# Display setup (assuming 128x64 resolution)
i2c = I2C(0, scl=Pin(17), sda=Pin(16), freq=400000) 
display = sh1106.SH1106_I2C(128, 64, i2c)

def power_off():
    """Display a message and put the Pico into deep sleep."""
    display.fill(0)
    display.text("Apagando...", 0, 20)
    display.show()
    utime.sleep(2)
    deepsleep()  # Enter deep sleep mode

