# pip install pymongo --user
# pip install dnspython --user

from pymongo import MongoClient
from bson.json_util import dumps
import json
import gridfs
import os

client = MongoClient("mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority")


mongoDb = client.get_database('gms_data')
records = mongoDb.status
sensorList = mongoDb.sensors

print (records.count_documents({}))

new_status = {
    'type':"tempHumid2",
    'firstLine':"Temperature: 25c",
    'secondLine':"Humidity: 50%"
}

# records.insert_one(new_status)  #many

# print(list(records.find({'type': 'tempHumid'})))

status_update ={
    'type': 'tempHumid',
    'image': "http://grid.com/aaa.jpg"
}

# records.update({'type':'tempHumid'}, status_update) #overwrite
# records.update_one({'type':'tempHumid2'}, {'$set': status_update})

# print (os.path)

# fs = gridfs.GridFS(db)

# image = 


a = (sensorList.find({'key': 'rfid'}))

b = records.find({'type': 'tempHumid'})
rfid= (sensorList.find_one({'key': 'rfid'}))
jsonRfid = dumps(rfid)
# d = json.loads(jsonRfid)

print(json.loads(jsonRfid)['value'])

# for i in d
#     print(i)

motor = sensorList.find_one({'key': 'motor'})
jsonMotor = dumps(motor)
cValue = json.loads(jsonMotor)['cycle']

print(int(cValue)*2)