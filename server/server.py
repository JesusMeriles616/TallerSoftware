from flask import Flask, render_template
from jugadores import jugadores_bp  # Import the blueprint
from juegos import juegos_bp
from color import color_bp

app = Flask(__name__)

# Register the jugadores blueprint
app.register_blueprint(jugadores_bp)

app.register_blueprint(juegos_bp)

app.register_blueprint(color_bp)

@app.route('/')
def index():
    return render_template('index.html')


if __name__ == '__main__':
    app.run(debug=True)

