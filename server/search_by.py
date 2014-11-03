import webapp2
from database import Database

class MainHandler(webapp2.RequestHandler):
    def get(self):
        session_id = self.request.get("id")
        search_creator = self.request.get("creator")
        search_users = self.request.get("users")
        search_recent = self.request.get("recent")

        db = Database(connect=1)
        #structure of session data: "id;creator;users;create_time"
        if search_creator and db.isStringSafe(search_creator):
            cursor = db.cursor()
            cursor.execute('SELECT id, users FROM data WHERE creator = "%s"' % search_creator)
            for row in cursor.fetchall():
                self.response.out.write("{0};{1};{2};255".format(row[0], search_creator, '1'))
        elif search_users:
            self.response.out.write("not implemented")
        elif search_recent:
            self.response.out.write("not implemented")
        else:
            self.response.out.write("not implemented")

app = webapp2.WSGIApplication([('/search_by.py?', MainHandler)],
                              debug=True)