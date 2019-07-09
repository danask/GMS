# author: Seongku (Daniel) Ahn
# date: May-15, 2019

import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from firebase_admin import storage
from threading import Thread

from picamera import PiCamera
from gpiozero import MotionSensor
from time import sleep
from datetime import datetime


import RPi.GPIO as GPIO
import dht11
import time
import datetime

# initialize GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(20, GPIO.IN)

#GPIO.cleanup()
        




PATH_CRED = '/home/pi/python-fire/gms-rasp.json'
URL_DB = 'https://gms-rasp.firebaseio.com/'
URL_STR = 'gms-rasp.appspot.com'


REF_DHT = 'DHT'
REF_PIR = 'PIR'

REF_TEMP = 'Temperature'
REF_HUMID = 'Humidity'

REF_ALERT = 'alert'
REF_ID = 'id'
REF_TIME = 'detectTime'

class TEST():

    def __init__(self):
        
        cred = credentials.Certificate(PATH_CRED)

        firebase_admin.initialize_app(cred, {
            'databaseURL': URL_DB,
            'storageBucket': URL_STR
        })

        self.msg = db.reference('timestamp')
        
        self.dht = db.reference(REF_DHT)
        self.pir = db.reference(REF_PIR)
        
        self.temp = self.dht.child(REF_TEMP)
        self.humid = self.dht.child(REF_HUMID)
        
        self.alert = self.pir.child(REF_ALERT)
        self.id = self.pir.child(REF_ID)
        self.time = self.pir.child(REF_TIME)
        
        print(self.msg.get())
        
        print(self.dht.get())
        print(self.temp.get())
        print(self.humid.get())
        
        #self.msg.set('testsend2')
        #print(self.msg.get())            
        
        # read data using pin 21
        instance = dht11.DHT11(pin=21)
        pir = MotionSensor(20)        
        camera = PiCamera()
            
        while True:
            
            currentTime = str(datetime.datetime.now())
            result = instance.read()
            if result.is_valid():
                print("Last valid input: " + currentTime)
                print("Temperature: %d C" % result.temperature)
                print("Humidity: %d %%" % result.humidity)
                
                self.msg.set(currentTime)
                self.temp.set(result.temperature)
                self.humid.set(result.humidity)
        
            #if GPIO.input(20):
            #    print("Motion detected...")

            state = GPIO.input(20)
            
            #db = firestore.client()
            bucket = storage.bucket()
            print bucket
        
            if state==0:
                print "nothing..."
                #self.alert.set("false")
                time.sleep(0.1)
            elif state==1:
                print "something here..."
                self.alert.set("true")

		detectTime = str(datetime.datetime.now())

                self.time.set(detectTime)
                                
                camera.capture('/home/pi/python-fire/Pics/imagetest.jpg')
                
                blobName = 'Pics/'+ detectTime + '.jpg'
                blob = bucket.blob(blobName)
                blob.upload_from_filename(filename='/home/pi/python-fire/Pics/imagetest.jpg')
                                
                self.id.set(blob.public_url)
                print(blob.public_url) # update this to db
                               
            #pir.wait_for_motion
            #print("Alert")
            
            #camera.start_preview()

            #for i in range (0, 5):
             #   sleep(5)
            #camera.capture('/home/pi/python-fire/Pics/imagetest.jpg')
            #camera.stop_preview()
            
                    
            time.sleep(2)
        
        print('GET Okay!!')
        
    def setDhtDB(self):
        self.msg.set('testsend')
        print(self.msg.get())            
        
        
        
# Start
test = TEST()

#msg = Thread(target= test.setDhtDB)
#msg.daemon = True
#msg.start()
