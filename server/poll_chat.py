import webapp2
from time import sleep, time
from database import Database
from urllib import quote_plus
from google.appengine.runtime import DeadlineExceededError

class MainHandler(webapp2.RequestHandler):
    def get(self):
        db = Database(connect=1)
        message = self.request.get("message")
        session_id = self.request.get("id")
        if session_id and db.sessionExists(session_id):
            if not message and session_id:
                start_poll = time() # get timestamp of poll started
                new_messages = []
                while 1:
                    try:
                        db = Database(connect=1) # reconnecting every time is necessary
                        chat = db.deserialize(db.getData("chat", session_id))
                        for message in chat:
                            if message:
                                message_timestamp = float(message.split(":")[0])
                                if message_timestamp > start_poll:
                                    new_messages.append(message)
                        if new_messages:
                            self.response.out.write(new_messages)
                            break
                        else:
                            sleep(2)
                    except DeadlineExceededError:
                        break
            elif message and db.isIDValid(session_id):
                current_chat = db.deserialize(db.getData("chat", session_id))
                current_chat.append(str(time()) + ":" + quote_plus(message))
                if len(current_chat >= 10):
                    current_chat.pop()
                db.setData("chat", db.serialize(current_chat), session_id)

app = webapp2.WSGIApplication([('/poll_chat.py?', MainHandler)],
                              debug=True)