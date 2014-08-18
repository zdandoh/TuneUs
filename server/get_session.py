import cgi
import sys
from database import Database
from re import match

def isIDValid(session_id):
    valid = False
    if session_id != None:
        if match("^[A-Za-z0-9_-]*$", str(session_id)) and len(session_id) == 40:
            valid = True
        else:
            print 'id not valid'
    return valid

if __name__ == "__main__":
    sys.stderr = sys.stdout
    db = Database()

    form = cgi.FieldStorage()
    data = {"id": False,
            "creator": False,
            "admins": False,
            "users": False,
            "queue": False,
            "chat": False
            }
    sent_field = form.getvalue("id")
    if db.isIDValid(sent_field):
        data["id"] = sent_field
    for field in data.keys():
        sent_field = form.getvalue(field)
        if sent_field and field != "id" and int(sent_field) == 1:
            data[field] = True

    print
    if data["id"]:
        #query db for data if id is valid
        db.connect()
        for field in data.keys():
            if data[field] and field != "id":
                query_data = db.getData(field, data["id"])
                print field + ":" + query_data
        db.close()