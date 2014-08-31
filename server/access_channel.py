import jinja2
import os
import webapp2
from unicodedata import normalize
from google.appengine.api import channel

#UNUSED CHANNELS API CODE
class MainPage(webapp2.RequestHandler):
    def get(self):
        channel_key = self.request.get('key')
        token = self.request.get('token')
        message = self.request.get('message')
        if not channel_key:
            self.response.out.write("no key")
            return
        if channel_key and not message:
            token = channel.create_channel(channel_key)
            write_dict = {"channelKey": channel_key, "token": token.encode('ascii','ignore')} #removes unicode characters
            self.response.out.write('{"token": "%s", "channelKey": "%s"}' % (token, channel_key))
        elif token and message:
            channel.send_message(token, message)


jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))
app = webapp2.WSGIApplication([('/create_channel.py', MainPage)],
                              debug=True)