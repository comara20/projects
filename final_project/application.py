import os
import jinja2
import re
from flask import Flask, jsonify, render_template, request, url_for
from flask_jsglue import JSGlue
from cs50 import SQL
from flask import Flask, flash, redirect, render_template, request, session, url_for
from flask_session import Session
from passlib.apps import custom_app_context as pwd_context
from tempfile import gettempdir
# from google.appengine.api import users


# configure application
app = Flask(__name__)
JSGlue(app)

env = jinja2.Environment(
	loader=jinja2.FileSystemLoader("templates"))

# ensure responses aren't cached
if app.config["DEBUG"]:
    @app.after_request
    def after_request(response):
        response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
        response.headers["Expires"] = 0
        response.headers["Pragma"] = "no-cache"
        return response

# configure CS50 Library to use SQLite database
db = SQL("sqlite:///mashup.db")

@app.route("/")
def index():
    """Render checkerboard."""
    return render_template("index.html")

'''
@app.route("/login")
def login():
    
   ''' 