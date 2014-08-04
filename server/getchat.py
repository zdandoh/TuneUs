#!/usr/bin/env python

import sys
import cgi
import os
sys.stderr = sys.stdout

form = cgi.FieldStorage()
session_id = form.getvalue("id")
count = form.getvalue("count")
if count > 50:
	count = 50

if os.path.exists('sessions/' + session_id):
	# get session
	pass
