import webapp2
import sys
from database import Database
from re import match

class MainHandler(webapp2.RequestHandler):
    def get(self):
        def isIDValid(session_id):
            valid = False
            if session_id != None:
                if match("^[A-Za-z0-9_-]*$", str(session_id)) and len(session_id) == 40:
                    valid = True
                else:
                    self.response.out.write('id not valid')
            return valid

        if __name__ == "__main__":
            sys.stderr = sys.stdout
            db = Database()

            data = {"id": False,
                    "creator": False,
                    "admins": False,
                    "users": False,
                    "queue": False,
                    "chat": False
                    }
            sent_field = self.request.get("id")
            if db.isIDValid(sent_field):
                data["id"] = sent_field
            for field in data.keys():
                sent_field = self.request.get(field)
                if sent_field and field != "id" and int(sent_field) == 1:
                    data[field] = True

            if data["id"]:
                #query db for data if id is valid
                db.connect()
                for field in data.keys():
                    if data[field] and field != "id":
                        query_data = db.getData(field, data["id"])
                        self.response.out.write(field + ":" + query_data)
                db.close()

app = webapp2.WSGIApplication([('/get_session.py?', MainHandler)],
                              debug=True)