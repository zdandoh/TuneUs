import cgi
import os
import urllib
import webapp2

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
            if len(db.getData("queue", session_id)) < 10:
                upload_url = blobstore.create_upload_url('/blob/upload')
                self.response.out.write(upload_url)
            else:
                self.response.out.write("queue limit reached")
        else:
            self.response.out.write("invalid session id")

class UploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        upload_files = self.get_uploads('file')  # 'file' is file upload field in the form
        session_id = self.request.get('id')
        blob_info = upload_files[0]

        db = Database(connect=1)
        new_queue = db.deserialize(db.getData("queue", session_id))
        new_queue.append(str(blob_info.key()))
        db.setData("queue", db.serialize("{0}:{1}".format(int(time()), new_queue)), session_id)
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
