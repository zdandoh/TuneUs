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
    return int(json_data["data"]["duration"])

form = FieldStorage()

session_id = form.getvalue("id")
video_id = form.getvalue("video")
db = Database(connect=1)
if db.sessionExists(session_id) and db.isStringSafe(video_id):
    old_queue = db.deserialize(db.getData("queue", session_id))
    duration = getVideoDuration(video_id)
    timestamp = int(time())
    play_time = timestamp + 2
    if old_queue:
        last_item = old_queue[-1].split(":")
        last_song_duration = last_item[2]
        last_song_playtime = last_item[3]
        play_time = int(last_song_playtime) + int(last_song_duration)
        if play_time < timestamp:
            play_time = timestamp + 2
    old_queue.append("%s:yt;%s:%s:%s" % (timestamp, video_id, duration, play_time))
    db.setData("queue", db.serialize(old_queue), session_id)