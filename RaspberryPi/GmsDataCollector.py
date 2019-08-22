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
import lcddriver
import dht11
import time
import datetime



# initialize GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(20, GPIO.IN)
GPIO.setup(26, GPIO.OUT) #Buzzer
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
        lcd = lcddriver.lcd()
        camera = PiCamera()
        
        lcd.lcd_display_string("GMS Agent v1.0", 1)
        lcd.lcd_display_string("     by Daniel", 2)
        time.sleep(3)
        
        lcd.lcd_clear()
        lcd.lcd_display_string("Hello, Admin!", 1)
        lcd.lcd_display_string("Loading...", 2)
        
        time.sleep(2) # to stabilize sensor
        
        while True:
            
            currentTime = str(datetime.datetime.now())
            result = instance.read()

            if result.is_valid():
                print("Last valid input: " + currentTime)
                
                tempStr = "Temperature: "+  str(result.temperature) + "c"
                humidStr = "Humidity: "+ str(result.humidity) +"%"
                
                #print("Temperature: %d C" % result.temperature)
                #print("Humidity: %d %%" % result.humidity)
                print(tempStr)
                print(humidStr)
                print("Writing to display")
                lcd.lcd_clear()
                lcd.lcd_display_string(tempStr, 1)
                lcd.lcd_display_string(humidStr, 2)
                
                ##self.msg.set(currentTime)
                ##self.temp.set(result.temperature)
                ##self.humid.set(result.humidity)
        
            #if GPIO.input(20):
            #    print("Motion detected...")

            state = GPIO.input(20)
            
            #db = firestore.client()
            bucket = storage.bucket()
            print bucket
        
            if state==0:
                print "nothing..."
                #self.alert.set("false")
                time.sleep(0.5)
            elif state==1:
                print "something's here..."
                lcd.lcd_clear()
                lcd.lcd_display_string("[Alert]", 1)
                lcd.lcd_display_string("Motion Detect", 2)
                
                GPIO.output(26, True)
                time.sleep(0.5)
                GPIO.output(26, False)
                
                ##self.alert.set("Motion Detect")

                detectTime = str(datetime.datetime.now())

                ##self.time.set(detectTime)
                                
                camera.capture('/home/pi/python-fire/Pics/imagetest.jpg')
                
                blobName = 'CapturedImages/'+ detectTime + '.jpg'
                blob = bucket.blob(blobName)
                ##blob.upload_from_filename(filename='/home/pi/python-fire/Pics/imagetest.jpg')
                                
                ##self.id.set(blob.public_url)
                print(blob.generate_signed_url(datetime.timedelta(seconds=300), method='GET')) # update this to db
                #self.id.set(blob.generate_signed_url(datetime.timedelta(seconds=300), method='GET'))
                time.sleep(3) # to avoid multiple detections                
            #pir.wait_for_motion
            #print("Alert")
            
            #camera.start_preview()

            #for i in range (0, 5):
             #   sleep(5)
            #camera.capture('/home/pi/python-fire/Pics/imagetest.jpg')
            #camera.stop_preview()
            
                    
            time.sleep(2) # loop delay, should be less than detection delay
        print('GET Okay!!')
        
    def setDhtDB(self):
        self.msg.set('testsend')
        print(self.msg.get())            
        
        
        
# Start
test = TEST()

#msg = Thread(target= test.setDhtDB)
#msg.daemon = True
#msg.start()

