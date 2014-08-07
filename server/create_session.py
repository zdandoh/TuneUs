import sys
import cgi
import hashlib
import database
from uuid import uuid4
from re import match

def isUserValid(username):
	un_valid = False
	if len(username) < 3:
		print 'username too short'
	elif len(username) > 16:
		print 'username too long'
	elif not match("^[A-Za-z0-9_-]*$", username):
		print 'username not valid'
	else:
		un_valid = True
	return un_valid

def getUniqueID():
	unique_id = uuid4()
	m = hashlib.sha1()
	m.update(str(unique_id))
	unique_hash = m.hexdigest()
	return unique_hash

if __name__ == "__main__":
	sys.stderr = sys.stdout

	form = cgi.FieldStorage()
	username = form.getvalue("user") # used to set admin

	if isUserValid(username):
		#return a new session id and gather all info
		session_id = getUniqueID()
		db = database.connect()
		cursor = db.cursor()
		cursor.execute('INSERT INTO data (id, creator, users) VALUES (%s, %s, %s)', (session_id, username, username + ','))
		db.commit()
		db.close()
		print session_id