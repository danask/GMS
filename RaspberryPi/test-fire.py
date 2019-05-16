# author: Seongku (Daniel) Ahn
# date: May-15, 2019

import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from threading import Thread

import RPi.GPIO as GPIO
import dht11
import time
import datetime

# initialize GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.cleanup()



PATH_CRED = '/home/pi/python-fire/gms-rasp.json'
URL_DB = 'https://gms-rasp.firebaseio.com/'

REF_DHT = 'DHT'
REF_TEMP = 'Temperature'
REF_HUMID = 'Humidity'


class TEST():

    def __init__(self):
        
        cred = credentials.Certificate(PATH_CRED)

        firebase_admin.initialize_app(cred, {
            'databaseURL': URL_DB
        })

        self.msg = db.reference('timestamp')
        
        self.dht = db.reference(REF_DHT)
        self.temp = self.dht.child(REF_TEMP)
        self.humid = self.dht.child(REF_HUMID)
        
        print(self.msg.get())
        
        print(self.dht.get())
        print(self.temp.get())
        print(self.humid.get())
        
        self.msg.set('testsend2')
        print(self.msg.get())            
        
        # read data using pin 21
        instance = dht11.DHT11(pin=21)

        while True:
            result = instance.read()
            if result.is_valid():
                print("Last valid input: " + str(datetime.datetime.now()))
                print("Temperature: %d C" % result.temperature)
                print("Humidity: %d %%" % result.humidity)
                
                self.msg.set(str(datetime.datetime.now()))
                self.temp.set(result.temperature)
                self.humid.set(result.humidity)
                
            time.sleep(1)
        
        print('GET Okay!!')
        
    def setDhtDB(self):
        self.msg.set('testsend')
        print(self.msg.get())            
        
        
        
# Start
test = TEST()

#msg = Thread(target= test.setDhtDB)
#msg.daemon = True
#msg.start()
