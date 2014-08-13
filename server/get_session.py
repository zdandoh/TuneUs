import cgi
import sys
import database
from re import match

def isIDValid(session_id):
    valid = False
    if session_id != None:
        if match("^[A-Za-z0-9_-]*$", str(session_id)) and len(session_id) == 40:
            valid = True
        else:
            print 'id not valid'
    return valid

def sessionExists(session_id):
    valid = False
    if isIDValid(session_id):
        db = database.connect()
        cursor = db.cursor()
        cursor.execute('SELECT creator FROM data WHERE id = "%s"' % (session_id))
        for row in cursor.fetchall():
            if row[0] != None:
                valid = True
                break
    return valid

if __name__ == "__main__":
    sys.stderr = sys.stdout

    form = cgi.FieldStorage()
    data = {"id": False,
            "creator": False,
            "admins": False,
            "users": False,
            "queue": False,
            "chat": False
            }
    sent_field = form.getvalue("id")
    if isIDValid(sent_field):
        data["id"] = sent_field
    for field in data.keys():
        sent_field = form.getvalue(field)
        if sent_field and field != "id" and int(sent_field) == 1:
            data[field] = True

    print
    if data["id"]:
        #query db for data if id is valid
        db = database.connect()
        cursor = db.cursor()
        for field in data.keys():
            if data[field] and field != "id":
                query_string = 'SELECT %s FROM data WHERE id = "%s"' % (field, data["id"])
                cursor.execute(query_string)
                for row in cursor.fetchall():
                    print field + ":" + str(row[0])