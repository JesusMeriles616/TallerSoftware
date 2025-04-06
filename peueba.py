def obtener_entero(mensaje="Ingrese un número entero: "):
    while True:
        try:
            valor = int(input(mensaje))
            return valor
        except ValueError:
            print("¡Valor inválido! Por favor ingrese solo números enteros.")

# Ejemplo de uso:
if __name__ == "__main__":
    edad = obtener_entero("Por favor ingrese su edad (número entero): ")
    print(f"Ha ingresado correctamente la edad: {edad}")