from database import Database
from time import sleep, time
from google.appengine.runtime import DeadlineExceededError
import logging
import webapp2

class MainHandler(webapp2.RequestHandler):
    def get(self):
        session_id = self.request.get("id")
        last_poll = self.request.get("last_poll")
        db = Database(connect=1)
        if db.sessionExists(session_id) and last_poll:
            new_songs = False
            try:
                while not new_songs:
                    queue = db.deserialize(db.getData("queue", session_id))
                    if len(queue):
                        queue = [item.split(":") for item in queue]
                        for item in queue:
                            if int(item[0]) > int(last_poll):
                                self.response.out.write(":".join(item))
                                new_songs = True
                    if not new_songs:
                        db.connect()
                        sleep(2)
            except DeadlineExceededError:
                self.response.out.write('queue check timed out')

app = webapp2.WSGIApplication([('/check_queue.py?', MainHandler)],
                              debug=True)
