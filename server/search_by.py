from cgi import FieldStorage
from database import Database

form = FieldStorage()
session_id = form.getvalue("id")
search_creator = form.getvalue("creator")
search_users = form.getvalue("users")
search_recent = form.getvalue("recent")

db = Database(connect=1)
print
#structure of session data: "id;creator;users;create_time"
if search_creator and db.isStringSafe(search_creator):
    cursor = db.cursor()
    cursor.execute('SELECT id, users FROM data WHERE creator = "%s"' % search_creator)
    for row in cursor.fetchall():
        print "{0};{1};{2};255".format(row[0], search_creator, '1')
elif search_users:
    print "not implemented"
elif search_recent:
    print "not implemented"
else:
    print "no valid data requested"