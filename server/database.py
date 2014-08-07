import MySQLdb

def connect(used_base="sessions"):
	instance_name = "tuneusserv:tuneusdata"
	db = MySQLdb.connect(unix_socket='/cloudsql/' + instance_name, db=used_base, user='root')
	return db