import RPi.GPIO as GPIO

from mfrc522 import SimpleMFRC522

reader = SimpleMFRC522()

try:
    text = raw_input('Enter New Data for writing to card:')
    print("Now place your tag to write")
    reader.write(text)
    print("Data was written successfully")
finally:
    GPIO.cleanup()
