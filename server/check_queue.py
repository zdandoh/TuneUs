from database import Database
from cgi import FieldStorage
from time import sleep, time
from google.appengine.runtime import DeadlineExceededError
import logging

form = FieldStorage()
session_id = form.getvalue("id")
last_poll = form.getvalue("last_poll")
db = Database(connect=1)
print 
if db.sessionExists(session_id) and last_poll:
    new_songs = False
    try:
        while not new_songs:
            queue = db.deserialize(db.getData("queue", session_id))
            if len(queue):
                queue = [item.split(":") for item in queue]
                for item in queue:
                    if int(item[0]) > int(last_poll):
                        print ":".join(item)
                        new_songs = True
            if not new_songs:
                db.connect()
                sleep(2)
    except DeadlineExceededError:
        print 'queue check timed out'