import sys
import cgi
import hashlib
import uuid

sys.stderr = sys.stdout
#print "Content-type: text/html\n\n";

form = cgi.FieldStorage()
new_session = form.getvalue("getnew")

def getUniqueID():
	unique_id = uuid.uuid4()
	m = hashlib.sha1()
	m.update(str(unique_id))
	unique_hash = m.hexdigest()
	return unique_hash

if int(new_session) == 1:
	#return a new session id
	print getUniqueID()