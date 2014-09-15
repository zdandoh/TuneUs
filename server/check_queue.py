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
    while not new_songs:
        try:
            queue = db.deserialize(db.getData("queue", session_id))
            if len(queue):
                queue = [item.split(":") for item in queue]
                for item in queue:
                    if int(item[0]) > int(last_poll):
                        print "(%s,%s)" % (item[0], item[1])
                        new_songs = True
            if not new_songs:
                db.connect()
                sleep(2)
        except DeadlineExceededError:
            print 'none' #this try/except doesn't actually do anything
            break