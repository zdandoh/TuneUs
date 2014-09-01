from cgi import FieldStorage
from database import Database

form = FieldStorage()

session_id = form.getvalue("id")
video_id = form.getvalue("video")
db = Database(connect=1)
if db.sessionExists(session_id) and db.isStringSafe(video_id):
    old_queue = db.deserialize(db.getData("queue", session_id))
    old_queue.append("yt:" + video_id)
    db.setData("queue", db.serialize(old_queue), session_id)