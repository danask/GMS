# author: Seongku (Daniel) Ahn
# date: Aug-15, 2019
# sudo python GmsDataCollector.py

# Camera, PIR, DHT11
import RPi.GPIO as GPIO
from picamera import PiCamera
from gpiozero import MotionSensor
import dht11

# 2 line-LCD 
import lcddriver
from mfrc522 import SimpleMFRC522

# time related library
import time
import datetime
from time import sleep
#from datetime import datetime

# firebase info. to use database and storage
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from firebase_admin import storage

# multi-threading
import threading

# mongoDB
# pip install pymongo --user
# pip install dnspython --user

from pymongo import MongoClient
from bson.json_util import dumps
import json
client = MongoClient("mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority")

# initialize GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM) 
GPIO.setup(20, GPIO.IN) # PIR
GPIO.setup(26, GPIO.OUT) # Buzzer


# Constants
PATH_CRED = '/home/pi/GMS/gms-rasp.json'
URL_DB = 'https://gms-rasp.firebaseio.com/'
URL_STR = 'gms-rasp.appspot.com'

REF_DHT = 'DHT'
REF_PIR = 'PIR'
REF_TEMP = 'Temperature'
REF_HUMID = 'Humidity'
REF_ALERT = 'alert'
REF_ID = 'id'
REF_TIME = 'detectTime'
REF_ADMIN = 46054386582

class GmsDataCollector():

    def __init__(self):
#def runSensors(num): 

        # firebase credential
        cred = credentials.Certificate(PATH_CRED)

        firebase_admin.initialize_app(cred, {
            'databaseURL': URL_DB,
            'storageBucket': URL_STR
        })
        bucket = storage.bucket()
        
        # mongodb        
        mongoDb = client.get_database('gms_data')
        records = mongoDb.status
        sensorList = mongoDb.sensors
        
        # get and test them
        self.msg = db.reference('timestamp')
        
        self.dht = db.reference(REF_DHT)
        self.pir = db.reference(REF_PIR)
        
        self.temp = self.dht.child(REF_TEMP)
        self.humid = self.dht.child(REF_HUMID)
        
        self.alert = self.pir.child(REF_ALERT)
        self.id = self.pir.child(REF_ID)
        self.time = self.pir.child(REF_TIME)
        
        print(self.msg.get()) #self.msg.set('testsend2')
        print(self.dht.get())
        print(self.temp.get())
        print(self.humid.get())
        

        # read data using pin 21(DHT11), 20 (PIR), LCD, Camera, RC522
        dht = dht11.DHT11(pin=21)
        pir = MotionSensor(20) 
        lcd = lcddriver.lcd()
        camera = PiCamera()
        reader = SimpleMFRC522() # StepMotor
        GPIO.output(26, False) # Buzzer to low
        
        # init. LCD
        lcd.lcd_display_string("GMS Agent v1.0", 1)
        lcd.lcd_display_string("     by Daniel", 2)
        
        status_update ={
                'firstLine': "GMS Agent v1.0",
                'secondLine': "     by Daniel"
        }
                    
        records.update_one({'type':'tempHumid'}, {'$set': status_update})
                            
        
        time.sleep(2)
        
        lcd.lcd_clear()
        lcd.lcd_display_string("Loading...", 1)
        lcd.lcd_display_string("", 2)
        
        status_update ={
                'firstLine': "Loading...",
                'secondLine': "     "
        }
                    
        records.update_one({'type':'tempHumid'}, {'$set': status_update})

        #print(records.count_documents({}))        
        #print(list(records.find({'type': 'tempHumid'})))
        
        time.sleep(2) # to stabilize sensor
        
        try:
            while True:
                
                # timestamp
                currentTime = str(datetime.datetime.now())
                tempHumid = dht.read()
                
                if tempHumid.is_valid():

                    print("**Update time: " + currentTime)
                    
                    tempStr = "Temperature: "+  str(tempHumid.temperature) + "c"
                    humidStr = "Humidity: "+ str(tempHumid.humidity) +"%"
                    #print("Temperature: %d C" % result.temperature)
                    #print("Humidity: %d %%" % result.humidity)

                    print(tempStr)
                    print(humidStr)
                    
                    status_update ={
                        'firstLine': tempStr,
                        'secondLine': humidStr
                    }
                    
                    records.update_one({'type':'tempHumid'}, {'$set': status_update})
                    
                    print("Writing to display LCD")
                    lcd.lcd_clear()
                    lcd.lcd_display_string(tempStr, 1)
                    lcd.lcd_display_string(humidStr, 2)
                    
                    # send them to firebase
                    ##self.msg.set(currentTime)
                    ##self.temp.set(result.temperature)
                    ##self.humid.set(result.humidity)
            

                state = GPIO.input(20)
            
                if state==0:
                    print ("nothing...")
                    time.sleep(0.5)

                elif state==1:
                    print ("something's here...")
                    lcd.lcd_clear()
                    lcd.lcd_display_string("[Alert]", 1)
                    lcd.lcd_display_string("Motion Detect", 2)
                    
                    status_update ={
                        'firstLine': "[Alert]",
                        'secondLine': "Motion Detect"
                    }
                    
                    records.update_one({'type':'tempHumid'}, {'$set': status_update})
                                        
                    
                    while True:
                        # RFID (2019/08/25)
                        GPIO.output(26, True)
                        time.sleep(0.5)
                                        
                        print("Hold a tag near the reader")
                        
                        rfid = sensorList.find_one({'key': 'rfid'})
                        jsonRfid = dumps(rfid)
                        pValue = json.loads(jsonRfid)['value']
                                                
                        id, text = reader.read()
                        print("ID: XXXXXXXXXXX\nText: %s" % text)
                        lcd.lcd_clear()        
                                        
                        if id == REF_ADMIN or pValue == "1234": # temp
                            GPIO.output(26, False)
                            clearMsg = "User: "+ str(text)
                            
                            lcd.lcd_display_string("Clear", 1)
                            lcd.lcd_display_string(clearMsg, 2)
                            
                            status_update ={
                                'firstLine': "Clear",
                                'secondLine': clearMsg
                            }
                            rfid_update={
                                'value': "0000"
                            }
                                            
                            records.update_one({'type':'tempHumid'}, {'$set': status_update})
                            sensors.update_one({'key':'rfid'}, {'$set': rfid_update})
                            
                            time.sleep(2)
                            break

                        else:
                            wrongMsg = "User: "+ str(text)
                            
                            lcd.lcd_display_string("Unauthorized", 1)
                            lcd.lcd_display_string(wrongMsg, 2)
                    
                            status_update ={
                                'firstLine': "Unauthorized",
                                'secondLine': wrongMsg
                            }
                            
                            records.update_one({'type':'tempHumid'}, {'$set': status_update})
                                         
                    
                    # update the record to firebase database
                    detectTime = str(datetime.datetime.now())
                    ##self.time.set(detectTime)
                    ##self.alert.set("Motion Detect")

                    # store the image in local space temporarily
                    camera.capture('/home/pi/GMS/Pics/imagetest.jpg')
                    
                    blobName = 'CapturedImages/'+ detectTime + '.jpg'
                    blob = bucket.blob(blobName)
                    
                    # upload the file to firebase storage
                    blob.upload_from_filename(filename='/home/pi/GMS/Pics/imagetest.jpg')
                    self.id.set(blob.public_url)
                    
                    time.sleep(1)

                    blobName = 'LastImage/lastImage.jpg'
                    blob = bucket.blob(blobName)

                    # upload the file to firebase storage
                    blob.upload_from_filename(filename='/home/pi/GMS/Pics/imagetest.jpg')
                    self.id.set(blob.public_url) 

                    # update this to check
                    #print(blob.generate_signed_url(datetime.timedelta(seconds=300), method='GET')) 
                    ##self.id.set(blob.generate_signed_url(datetime.timedelta(seconds=300), method='GET'))
                    time.sleep(2) # to avoid multiple detections                
                        
                time.sleep(2) # loop delay, should be less than detection delay
                
        except KeyboardInterrupt:
            GPIO.cleanup()

        #print("Done!") 


class GmsWateringMotor():

    def __init__(self):


# def wateringMotor(num):
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

         # rotation
        wateringDuration = 10

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
    
   
def setDhtDB(self):
    self.msg.set('testsend')
    print(self.msg.get())            
        

def print_cube2(num): 
    print("Cube: {}".format(num * num * num)) 

def print_square(num): 
    print("Square: {}".format(num * num)) 



#def __init__(self):
#if __name__ == "__main__":     

 #   try:
        #t1 = threading.Thread(target=runSensors, args=(10,)) 
 #       t2 = threading.Thread(target=wateringMotor, args=(10,)) 
      
        #t1.start() 
 #       t2.start() 
    
#    except KeyboardInterrupt:
#        GPIO.cleanup()
        
# Start
#gmsWateringMotor = GmsWateringMotor()
gmsDataCollector = GmsDataCollector()


#msg = Thread(target= test.setDhtDB)
#msg.daemon = True
#msg.start()



