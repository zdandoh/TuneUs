import sys
import cgi
import hashlib
from uuid import uuid4

sys.stderr = sys.stdout

form = cgi.FieldStorage()
username = form.getvalue("user") # used to set admin

def getUniqueID():
	unique_id = uuid4()
	m = hashlib.sha1()
	m.update(str(unique_id))
	unique_hash = m.hexdigest()
	return unique_hash

#return a new session id
session_id = getUniqueID()
