import MySQLdb
from re import match

class Database():
    def __init__(self, connect=False):
        self.connected = False
        self.db = None
        if connect:
            self.connect()

    def serialize(self, data):
        data = ",".join(data)
        return data

    def deserialize(self, data):
        data = data.split(",")
        return data

    def isIDValid(self, session_id, length_check=True):
        valid = False
        if session_id != None:
            if match("^[A-Za-z0-9_-]*$", str(session_id)) and (not length_check or len(session_id) == 40):
                valid = True
        return valid

    def isStringSafe(self, string):
        return self.isIDValid(string, length_check=False)

    def sessionExists(self, session_id):
        valid = False
        if self.isIDValid(session_id) and self.getData("id", session_id):
            valid = True
        return valid

    def getData(self, field, session_id):
        if self.isIDValid(session_id) and self.connected:
            cursor = self.db.cursor()
            query_string = 'SELECT %s FROM data WHERE id = "%s"' % (field, session_id)
            cursor.execute(query_string)
            for row in cursor.fetchall():
                raw_result = row[0]
                if raw_result != None:
                    return str(raw_result)
                else:
                    return ""

    def setData(self, field, new_data, session_id):
        """This function won't check new_data, make sure it is safe!"""
        if self.isIDValid(session_id):
            cursor = self.db.cursor()
            query_string = 'UPDATE data SET %s="%s" WHERE id = "%s"' % (field, new_data, session_id)
            cursor.execute(query_string)
            self.db.commit()

    def connect(self, used_base="sessions"):
        instance_name = "tuneusserv:tuneusdata"
        self.db = MySQLdb.connect(unix_socket='/cloudsql/' + instance_name, db=used_base, user='root')
        self.connected = True

    def close(self):
        self.db.close()

    def commit(self):
        self.db.commit()

    def cursor(self):
        cursor = self.db.cursor()
        return cursor