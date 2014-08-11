import os
import urllib
import webapp2
 
from google.appengine.ext import blobstore
from google.appengine.ext.webapp import blobstore_handlers
from google.appengine.ext.webapp.util import run_wsgi_app
 
class MainHandler(webapp2.RequestHandler):
  def get(self):
    upload_url = blobstore.create_upload_url('/blob/upload')
    self.response.out.write(upload_url)
 
class UploadHandler(blobstore_handlers.BlobstoreUploadHandler):
  def post(self):
    upload_files = self.get_uploads('file')  # 'file' is file upload field in the form
    blob_info = upload_files[0]
    self.response.out.write(blob_info.key())
    #self.redirect('/serve/%s' % blob_info.key())

class ServeHandler(blobstore_handlers.BlobstoreDownloadHandler):
  def get(self, resource):
    resource = str(urllib.unquote(resource))
    blob_info = blobstore.BlobInfo.get(resource)
    self.send_blob(blob_info)
 
app = webapp2.WSGIApplication([('/blob/upload_blob.py', MainHandler),
                               ('/blob/upload', UploadHandler),
                               ('/blob/serve/([^/]+)?', ServeHandler)],
                              debug=True)
