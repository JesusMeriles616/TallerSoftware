import keypad  # Import the keypad module
import login_menu  # Import the menu module
import power  # Import the power module
import utime
import wifi  # Import the Wi-Fi connection module
import urequests  # Import urequests to handle HTTP requests
import ujson  # Import ujson to handle JSON serialization
from secrets import secrets  # Import secrets to access base_url
from game_menu import show_game_menu  # Import the game menu function

# Initialize the keypad
keypad.init_keypad()

# Attempt to connect to the network before proceeding
if wifi.connect_to_network():
    # Main loop
    login_menu.show_login_menu()  # Display the menu at startup

    while True:
        key = keypad.scan_key()  # Get the key press from the keypad
        if key == '1':
            # Call the input_credentials function to get username and password
            username, password = login_menu.input_credentials()
            
            # Send the login request
            send_login_request(username, password)
            
            # Show menu again after sending credentials
            login_menu.show_login_menu()
        elif key == '2':
            power.power_off()  # Call power off function when '2' is pressed
else:
    print("Unable to proceed without network connection.")
    
    
    
# Function to send login request
def send_login_request(username, password):
    url = f"{secrets['base_url']}/account_http.php"
    headers = {'Content-Type': 'application/json'}
    payload = {
        "username": username,
        "pwd": password
    }
    
    print("JSON Payload:", ujson.dumps(payload))
    
    # Attempt to send the POST request
    try:
        # Use ujson.dumps to ensure correct JSON encoding
        response = urequests.post(url, data=ujson.dumps(payload), headers=headers)
        
        # Display server response
        if response.status_code == 200:
            # Successful login
            login_menu.display.fill(0)
            login_menu.display.text("Bienvenido!", 0, 20)
            login_menu.display.show()
            utime.sleep(2)  # Show "Bienvenido" message briefly
            show_game_menu(login_menu.display)  # Redirect to the game menu
            
        elif response.status_code == 401:
            # Unauthorized - invalid credentials
            login_menu.display.fill(0)
            login_menu.display.text("Login fallido", 0, 20)
            login_menu.display.text("Usuario o contraseña", 0, 40)
            login_menu.display.text("incorrectos.", 0, 52)
            login_menu.display.show()
            utime.sleep(2)
            
        elif response.status_code == 400:
            # Bad request - missing fields
            print("Error: Falta usuario o contraseña.")
            login_menu.display.fill(0)
            login_menu.display.text("Error:", 0, 20)
            login_menu.display.text("Falta usuario o", 0, 40)
            login_menu.display.text("contraseña.", 0, 52)
            login_menu.display.show()
            utime.sleep(2)
            
        else:
            # Other server issue
            print("Error: Server issue")
            login_menu.display.fill(0)
            login_menu.display.text("Error:", 0, 20)
            login_menu.display.text("Server issue", 0, 40)
            login_menu.display.show()
            utime.sleep(2)
        response.close()
        utime.sleep(2)
        
    except Exception as e:
        print("Error:", e)
        login_menu.display.fill(0)
        login_menu.display.text("Error:", 0, 20)
        login_menu.display.text(str(e), 0, 40)
        login_menu.display.show()
        utime.sleep(2)

