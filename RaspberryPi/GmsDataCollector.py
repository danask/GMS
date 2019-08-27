# author: Seongku (Daniel) Ahn
# date: Aug-15, 2019

# Camera, PIR, DHT11
import RPi.GPIO as GPIO
from picamera import PiCamera
from gpiozero import MotionSensor
import dht11

# 2 line-LCD 
import lcddriver
from mfrc522 import SimpleMFRC522

# time related library
from time import sleep
from datetime import datetime
#import time
#import datetime

# firebase info. to use database and storage
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from firebase_admin import storage

# multi-threading
from threading import Thread

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


class GMSSensors():

    def __init__(self):
        
        # firebase credential
        cred = credentials.Certificate(PATH_CRED)

        firebase_admin.initialize_app(cred, {
            'databaseURL': URL_DB,
            'storageBucket': URL_STR
        })

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
        pir = GPIO.input(20) #MotionSensor(20)
        lcd = lcddriver.lcd()
        camera = PiCamera()
        reader = SimpleMFRC522() # StepMotor

        # init. LCD
        lcd.lcd_display_string("GMS Agent v1.0", 1)
        lcd.lcd_display_string("     by Daniel", 2)
        time.sleep(3)
        
        lcd.lcd_clear()
        lcd.lcd_display_string("Loading...", 1)
        lcd.lcd_display_string("", 2)
        
        time.sleep(2) # to stabilize sensor
        
        while True:
            
            # timestamp
            currentTime = str(datetime.datetime.now())
            
            if dht.read().is_valid():

                print("Last valid input: " + currentTime)
                
                tempStr = "Temperature: "+  str(result.temperature) + "c"
                humidStr = "Humidity: "+ str(result.humidity) +"%"
                #print("Temperature: %d C" % result.temperature)
                #print("Humidity: %d %%" % result.humidity)

                print(tempStr)
                print(humidStr)

                print("Writing to display LCD")
                lcd.lcd_clear()
                lcd.lcd_display_string(tempStr, 1)
                lcd.lcd_display_string(humidStr, 2)
                
                # send them to firebase
                ##self.msg.set(currentTime)
                ##self.temp.set(result.temperature)
                ##self.humid.set(result.humidity)
        

#            state = GPIO.input(20)
            
            #db = firestore.client()
            bucket = storage.bucket()
            print bucket
        
            if pir == 0:
                print ("nothing...")
                #self.alert.set("false")
                time.sleep(0.5)

            elif pir == 1:
                print ("something's here...")
                lcd.lcd_clear()
                lcd.lcd_display_string("[Alert]", 1)
                lcd.lcd_display_string("Motion Detect", 2)
                

                # RFID (2019/08/25)
                GPIO.output(26, True)
                time.sleep(0.5)
                                
                print("Hold a tag near the reader")
                id, text = reader.read()
                print("ID: %s\nText: %s" % (id,text))
                                
                if id == 46054386582: # temp
                    GPIO.output(26, False)
                    lcd.lcd_clear()
                    lcd.lcd_display_string("[Alert]", 1)
                    lcd.lcd_display_string("Clear by Admin", 2)
                    time.sleep(3)
                
                # update the record to firebase database
                detectTime = str(datetime.datetime.now())
                ##self.time.set(detectTime)
                ##self.alert.set("Motion Detect")

                # store the image in local space temporarily
                camera.capture('/home/pi/GMS/Pics/imagetest.jpg')
                
                blobName = 'CapturedImages/'+ detectTime + '.jpg'
                blob = bucket.blob(blobName)

                # upload the file to firebase storage
                ##blob.upload_from_filename(filename='/home/pi/python-fire/Pics/imagetest.jpg')
                ##self.id.set(blob.public_url) 

                # update this to check
                #print(blob.generate_signed_url(datetime.timedelta(seconds=300), method='GET')) 
                ##self.id.set(blob.generate_signed_url(datetime.timedelta(seconds=300), method='GET'))
                time.sleep(3) # to avoid multiple detections                
                    
            time.sleep(2) # loop delay, should be less than detection delay
        
    def setDhtDB(self):
        self.msg.set('testsend')
        print(self.msg.get())            
        
        
        
# Start
test = GMSSensors()

#msg = Thread(target= test.setDhtDB)
#msg.daemon = True
#msg.start()


