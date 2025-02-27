import urequests
import ujson
import network
import utime


def connect_to_wifi(ssid, password):
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(ssid, password)
    print("Connecting to Wi-Fi...", end="")

  
    while not wlan.isconnected():
        utime.sleep(1)
        print(".", end="")

    print("\nConnected to Wi-Fi")
    print("IP Address:", wlan.ifconfig()[0])


def send_game_data(id_game, color_list):
    url = "http://http://192.168.1.101:41062/color_game_endpoint.php"  
    headers = {'Content-Type': 'application/json'}

   
    payload = {
        "id_game": id_game,
        "color_list": color_list
    }

    try:
        response = urequests.post(url, data=ujson.dumps(payload), headers=headers)
        
    
        if response.status_code == 201:
            print("Game saved successfully:", response.json())
        elif response.status_code == 400:
            print("Bad Request:", response.json().get("message"))
        else:
            print("Server Error:", response.json().get("message"))
        
        response.close()
    except Exception as e:
        print("Request failed:", e)


ssid = "SSID"
password = "Password"
connect_to_wifi(ssid, password)


id_game = 123
color_list = [
    [1, "Rojo", 1],
    [2, "Azul", 0],
    [3, "Verde", 1]
]

send_game_data(id_game, color_list)

