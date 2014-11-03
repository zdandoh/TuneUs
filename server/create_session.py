import sys
import hashlib
import webapp2
from database import Database
from uuid import uuid4
from re import match

class MainHandler(webapp2.RequestHandler):
    def get(self):
        def isUserValid(username):
            un_valid = False
            if username == None: # check to make sure it's real
                return un_valid
            if len(username) < 3:
                self.response.out.write('username too long')
            elif len(username) > 16:
                self.response.out.write('username too long')
            elif not match("^[A-Za-z0-9_-]*$", username):
                self.response.out.write('username not valid')
            else:
                un_valid = True
            return un_valid

        def getUniqueID():
            unique_id = uuid4()
            m = hashlib.sha1()
            m.update(str(unique_id))
            unique_hash = m.hexdigest()
            return unique_hash

        sys.stderr = sys.stdout

        username = self.request.get("user") # used to set admin

        if isUserValid(username):
            #return a new session id and gather all info
            session_id = getUniqueID()
            db = Database(connect=1)
            cursor = db.cursor()
            cursor.execute('INSERT INTO data (id, creator, users) VALUES (%s, %s, %s)', (session_id, username, username))
            db.commit()
            db.close()
            self.response.out.write(session_id)

app = webapp2.WSGIApplication([('/create_session.py?', MainHandler)],
                              debug=True)