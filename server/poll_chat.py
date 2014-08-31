from cgi import FieldStorage
from time import sleep, time
from database import Database
from urllib import quote_plus
from google.appengine.runtime import DeadlineExceededError

db = Database(connect=1)
form = FieldStorage()
message = form.getvalue("message")
session_id = form.getvalue("id")
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
                    print
                    print new_messages
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