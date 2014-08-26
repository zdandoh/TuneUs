import jinja2
import os
import webapp2
from unicodedata import normalize
from google.appengine.api import channel

class MainPage(webapp2.RequestHandler):
    """This page is responsible for showing the game UI. It may also
    create a new game or add the currently-logged in user to a game."""

    def get(self):
        channel_key = self.request.get('key')
        message = self.request.get('message')
        if channel_key:
            token = channel.create_channel(channel_key)
            write_dict = {"channelKey": channel_key, "token": token.encode('ascii','ignore')} #removes unicode characters
            self.response.out.write('{"token": "%s", "channelKey": "%s"}' % (token, channel_key))
            if message:
                channel.send_message(channel_key, "poop")
        else:
            self.response.out.write("no key")


jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))
app = webapp2.WSGIApplication([('/create_channel.py', MainPage)],
                              debug=True)