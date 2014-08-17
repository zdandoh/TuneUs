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

def getData(field, session_id):
    if isIDValid(session_id):
        db = database.connect()
        cursor = db.cursor()
        query_string = 'SELECT %s FROM data WHERE id = "%s"' % (field, session_id)
        cursor.execute(query_string)
        for row in cursor.fetchall():
            return str(row[0])
        db.close()

def setData(field, new_data, session_id):
    """This function won't check new_data, make sure it is safe!"""
    if isIDValid(session_id):
        db = database.connect()
        cursor = db.cursor()
        query_string = 'UPDATE data SET %s="%s" WHERE id = "%s"' % (field, new_data, session_id)
        cursor.execute(query_string)
        db.commit()
        db.close()

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
        for field in data.keys():
            if data[field] and field != "id":
                query_data = getData(field, data["id"])
                print field + ":" + query_data