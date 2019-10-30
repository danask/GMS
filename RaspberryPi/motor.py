import RPi.GPIO as GPIO
import time

from pymongo import MongoClient
from bson.json_util import dumps
import json
client = MongoClient("mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority")

GPIO.setmode(GPIO.BOARD)
sensors = mongoDb.sensors


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
motor = sensors.find_one({'key': 'motor'})
jsonMotor = dumps(motor)
cValue = json.loads(jsonMotor)['cycle']

wateringDuration = int(cValue)

try:        
    while True:
              
        for times in range (wateringDuration):
            for i in range(256):
              for halfstep in range(8):
                for pin in range(4):
                  GPIO.output(control_pins[pin], halfstep_seq[halfstep][pin])
                time.sleep(0.001)
                
        wateringDuration = 0

        # check and get the rotation number from Firebase
                
        if wateringDuration == 0:
            print("Stand by: waiting for the watering input from GMS server...")
            time.sleep(10.0)
            #break;
            
except KeyboardInterrupt:
    GPIO.cleanup()
    raise
