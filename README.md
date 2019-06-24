# Gardening Management System (GMS)
### 1. Raspberry PI to collect the data
collects data source from sensors
- DHT
- PIR
- Camera
### 2. Firebase Database and Storage for storing the data
NoSQL database and file storage in the cloud
### 3. GMS Web Server for managing the whole system
Communication between back-end (Spring MVC) to front-end (Angular) via API calls
**Features**
- Login/out
- Dashboard: current info (weather information, recent data from sensors)
- History: motion/room sensor, monitor images, alert, recommendation action
- User Management
- Settings

**SaaS-based web application server for customers**
- User
- Administrator
### 4. Recommdendation module
analyzes the data sources
