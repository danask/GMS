# Gardening Management System with Remote Controllers

### Requirement ###
 - Useful application for our life
 - Integration technology as much as possible
 - Full stack development

### 1. Raspberry PI to collect the data (Python, IoT devices)
Collects data  from sensors and interact with user command 
- DHT
- PIR (motion sensor)
- Camera
- Step-moter
- LED-Buzzer
- 2Line-LCD
- RFID

### 2. GMS Web Server for managing the whole system (Spring MVC, Quartz, Angular6)

**Features**
- Login/out
- Dashboard: current info (weather information, recent data from sensors)
- Remote Controller: virtual controller as plug-in
- History: motion/room sensor, monitor images, alert, recommendation action
- User Management
- Settings: email notification

**SaaS-based web application server for customers**
- User
- Administrator

### 3. Persistent storage and database (MongoDB, Firebase Database and Storage)
NoSQL database and file storage in the cloud

### 4. Remote Controller (web: MERN stack, AWS, mobile: Android app)
- Client1 (admin): Control panel working in real-time using React, NodeJS, Socket.io, etc.
- Client2 (user): Android App

**Features**
- Watering control with recommendation
- Alarm dismiss
- Urgent message
- Action replay
- Chat

### 5. Recommdendation module (Python, Tensorflow for machine learning)
- Analyzes the data sources using Tensorflow and linear regression
- How much water do we need?


### Layout
![](https://github.com/danask/GMS/blob/master/Frontend/src/assets/img/gms_architecture_v2.PNG)

