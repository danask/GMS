import RPi.GPIO as GPIO
import time

from pymongo import MongoClient
from bson.json_util import dumps
import json
client = MongoClient("mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority")

GPIO.setmode(GPIO.BOARD)

mongoDb = client.get_database('gms_data')
sensorList = mongoDb.sensors


control_pins = [7,11,13,15]
for pin in control_pins:
  GPIO.setup(pin, GPIO.OUT)
  GPIO.output(pin, 0)
  
halfstep_seq = [
  [1,0,0,0],
  [1,1,0,0],
  [0,1,0,0],
  [0,1,1,0],
  [0,0,1,0],
  [0,0,1,1],
  [0,0,0,1],
  [1,0,0,1]
]

# rotation cycle

wateringCycle = 10

motor = sensorList.find_one({'key': 'motor'})
jsonMotor = dumps(motor)
cValue = json.loads(jsonMotor)['cycle']
isOff = json.loads(jsonMotor)['status']

if isOff == "off":
   wateringCycle = 0
else:
   print (cValue)
   wateringCycle = int(cValue)

try:        
    while True:
        status = "on"

        for times in range (wateringCycle):
            for i in range(256):
              for halfstep in range(8):
                for pin in range(4):
                  GPIO.output(control_pins[pin], halfstep_seq[halfstep][pin])
                time.sleep(0.001)

	    print("wateringCycle " + str(wateringCycle))
	    motor = sensorList.find_one({'key': 'motor'})
	    jsonMotor = dumps(motor)
            status = json.loads(jsonMotor)['status']
	    #time.sleep(0.005)
            print("status " + status)
            if status == "off":
               break
                
        wateringCycle = 0
	motor_update = {
		'cycle': "0"
	}
	sensorList.update_one({'key':'motor'}, {'$set': motor_update})

        # check and get the rotation number from db
                
        if wateringCycle == 0:
            print("Stand by: waiting for the watering input from GMS server...")
            time.sleep(5.0)

	    motor = sensorList.find_one({'key': 'motor'})
	    jsonMotor = dumps(motor)
            cValue = json.loads(jsonMotor)['cycle']
	    print (cValue)
	    wateringCycle = int(cValue)

            #break;
            
except KeyboardInterrupt:
    GPIO.cleanup()
    raise
