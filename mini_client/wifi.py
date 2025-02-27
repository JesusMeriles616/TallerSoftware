import network
import utime
from machine import I2C, Pin
from secrets import secrets
from lib import sh1106  # Adjust path if necessary

# Display setup (assuming 128x64 resolution)
i2c = I2C(0, scl=Pin(17), sda=Pin(16), freq=400000)
display = sh1106.SH1106_I2C(128, 64, i2c)

def show_message_on_display(message):
    """Helper function to display a multi-line message on the OLED."""
    display.fill(0)  # Clear the screen
    lines = message.split('\n')  # Split message by lines
    for i, line in enumerate(lines):
        display.text(line, 0, i * 12)  # Set each line with 12 pixels spacing
    display.show()

def connect_to_network():
    """Connect to the Wi-Fi network using credentials from secrets.py."""
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    
    if not wlan.isconnected():
        show_message_on_display("Conectando...")
        wlan.connect(secrets['ssid'], secrets['password'])
        
        # Wait for connection with a timeout
        timeout = 10  # seconds
        start_time = utime.time()
        while not wlan.isconnected():
            if utime.time() - start_time > timeout:
                show_message_on_display("Error Wifi")
                utime.sleep(2)
                return False
            utime.sleep(1)
    
    # Connection successful
    if wlan.isconnected():
        ip_address = wlan.ifconfig()[0]
        ssid_display = f"SSID: {secrets['ssid'][:10]}"  # Limit SSID to 10 characters
        ip_display = f"IP: {ip_address}"
        show_message_on_display(f"{ssid_display}\n{ip_display}")
        utime.sleep(2)
        return True


