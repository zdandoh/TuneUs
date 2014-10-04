from time import time
from cgi import FieldStorage
from database import Database
import logging
import urllib
import json

def getVideoDuration(video_id):
    if video_id.startswith("yt;"):
        video_id = video_id.replace("yt;", "", 1)
    API_URL = "http://gdata.youtube.com/feeds/api/videos/%s?v=2&alt=jsonc&prettyprint=true" % video_id
    video_json = urllib.urlopen(API_URL)
    json_data = json.load(video_json)
    return json_data["data"]["duration"]

form = FieldStorage()

session_id = form.getvalue("id")
video_id = form.getvalue("video")
db = Database(connect=1)
if db.sessionExists(session_id) and db.isStringSafe(video_id):
    old_queue = db.deserialize(db.getData("queue", session_id))
    duration = getVideoDuration(video_id)
    timestamp = int(time())
    old_queue.append("%s:yt;%s:%s" % (timestamp, video_id, duration))
    db.setData("queue", db.serialize(old_queue), session_id)