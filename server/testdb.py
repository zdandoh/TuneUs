import cgi
import database

db = database.connect()
cursor = db.cursor()
db.commit()
db.close()