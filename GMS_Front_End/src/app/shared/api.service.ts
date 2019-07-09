import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dashboard/model/user';
import {Sensor} from '../dashboard/model/sensor';
import {MotionSensor} from '../dashboard/model/motionSensor';
import {Weather} from '../dashboard/model/weather';
import {AngularFireDatabase, AngularFireList} from '@angular/fire/database';
import { AngularFireStorage } from '@angular/fire/storage';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private BASE_URL = 'http://localhost:8080/GMS';
  private WEATHER_BASE_URL = 'http://api.openweathermap.org/data/2.5/'

  private ALL_USERS_URL = this.BASE_URL + '\\User\\userAll';
  private GET_USER_URL = this.BASE_URL + '\\User\\getUserByEmailPwd';
  private ADD_USER_URL = this.BASE_URL + '\\User\\saveUserApi';

  private ALL_SENSORS_URL = this.BASE_URL + '\\Sensor\\sensorAll';
  private ALL_MOTION_SENSORS_URL = this.BASE_URL + '\\MotionSensor\\motionSensorAll';

  private LAST_SENSOR_URL = this.BASE_URL + '\\Sensor\\sensorLatestOne';
  private LAST_MOTION_SENSOR_URL = this.BASE_URL + '\\MotionSensor\\motionSensorLatestOne';

  private WEATHER_URL = this.WEATHER_BASE_URL +
                  'weather?q=Vancouver&APPID=1b5fcb8df3906c5092aca2b51707953b';

  imageDetailList: AngularFireList <any>;

  constructor(private http: HttpClient,
              private firebase: AngularFireDatabase,
              private firebaseStorage: AngularFireStorage ) {
  }

  getImageDetailList() {
    this.imageDetailList = this.firebase.list('PIR');
  }

  // image handling
  getMediaURL(image): Promise<any> {
    const storageRef = this.firebaseStorage.storage.ref().child(image);
    return storageRef.getDownloadURL().then(url => {
      console.log('firebase response: ' + url);
      return url;
    });
  }


  insertImageDetails(imageDetails){
    this.imageDetailList.push(imageDetails);
  }

  getUserAll(): Observable<User[]>{
    return this.http.get<User[]>(this.ALL_USERS_URL);
  }

  getUser(user: User): Observable<any>{
    return this.http.post(this.GET_USER_URL, user);
  }

  setUser(user: User): Observable<any>{
    return this.http.post(this.ADD_USER_URL, user);
  }

  getSensorAll(): Observable<Sensor[]>{
    return this.http.get<Sensor[]>(this.ALL_SENSORS_URL);
  }

  getMotionSensorAll(): Observable<MotionSensor[]>{
    return this.http.get<MotionSensor[]>(this.ALL_MOTION_SENSORS_URL);
  }

  getLastSensor(): Observable<Sensor>{
    return this.http.get<Sensor>(this.LAST_SENSOR_URL);
  }

  getLastMotionSensor(): Observable<MotionSensor>{
    return this.http.get<MotionSensor>(this.LAST_MOTION_SENSOR_URL);
  }

  getWeatherData(): Observable<Weather[]>{
    return this.http.get<Weather[]>(this.WEATHER_URL);
  }

}
