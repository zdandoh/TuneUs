import cgi
import os
import urllib
import webapp2
import logging

from time import time
from database import Database
from google.appengine.ext import blobstore
from google.appengine.ext.webapp import blobstore_handlers
from google.appengine.ext.webapp.util import run_wsgi_app

class MainHandler(webapp2.RequestHandler):
    def get(self):
        #check if session id is valid first
        db = Database(connect=1)
        form = cgi.FieldStorage()
        session_id = form.getvalue("id")
        if db.sessionExists(session_id):
            queue = db.deserialize(db.getData("queue", session_id))
            if len(queue) < 10:
                upload_url = blobstore.create_upload_url('/blob/upload')
                self.response.out.write(upload_url)
            else:
                self.response.out.write("queue limit reached")
                logging.debug(queue)
                logging.debug(len(queue))
        else:
            self.response.out.write("invalid session id")

class UploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        upload_files = self.get_uploads('file')  # 'file' is file upload field in the form
        session_id = self.request.get('id')
        song_length = self.request.get('length')
        blob_info = upload_files[0]

        db = Database(connect=1)
        new_queue = db.deserialize(db.getData("queue", session_id))
        timestamp = int(time())
        new_queue.append("{0}:{1}:{2}".format(timestamp, str(blob_info.key()), song_length))
        db.setData("queue", db.serialize(new_queue), session_id)
        self.response.out.write(blob_info.key())

class ServeHandler(blobstore_handlers.BlobstoreDownloadHandler):
    def get(self, resource):
        resource = str(urllib.unquote(resource))
        blob_info = blobstore.BlobInfo.get(resource)
        self.send_blob(blob_info)

app = webapp2.WSGIApplication([('/blob/upload_blob.py?', MainHandler),
                               ('/blob/upload', UploadHandler),
                               ('/blob/serve/([^/]+)?', ServeHandler)],
                              debug=True)
