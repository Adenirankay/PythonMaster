'''
Created on Feb 19, 2019

@author: LONGBRIDGE
'''
import cx_Oracle
import struct
print(struct.calcsize("P") * 8)

connection = cx_Oracle.connect('SYSTEM/manager11@10.0.33.13:/1721/HBNGDR')
myquery= "select * from tbaadm.gam where schm_type = 'SBA' "
cur = connection.cursor()
cur.execute('myquery')
print(myquery)
cur.close()
connection.close()

