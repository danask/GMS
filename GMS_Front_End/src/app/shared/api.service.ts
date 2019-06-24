import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dashboard/model/user';
import {Sensor} from '../dashboard/model/sensor';
import {MotionSensor} from '../dashboard/model/motionSensor';
import {Weather} from '../dashboard/model/weather';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private BASE_URL = 'http://localhost:8080/GMS';
  private WEATHER_BASE_URL = 'http://api.openweathermap.org/data/2.5/'

  private ALL_USERS_URL = this.BASE_URL + '\\User\\userAll';
  private ADD_USERS_URL = this.BASE_URL + '\\User\\saveUserApi';

  private ALL_SENSORS_URL = this.BASE_URL + '\\Sensor\\sensorAll';
  private ALL_MOTIONSENSORS_URL = this.BASE_URL + '\\MotionSensor\\motionSensorAll';

  private WEATHER_URL = this.WEATHER_BASE_URL +
                  'weather?q=Vancouver&APPID=1b5fcb8df3906c5092aca2b51707953b';

  constructor(private http: HttpClient) {

  }

  getUserAll(): Observable<User[]>{
    return this.http.get<User[]>(this.ALL_USERS_URL);
  }

  setUser(user: User): Observable<any>{
    return this.http.post(this.ADD_USERS_URL, user);
  }

  getSensorAll(): Observable<Sensor[]>{
    return this.http.get<Sensor[]>(this.ALL_SENSORS_URL);
  }

  getMotionSensorAll(): Observable<MotionSensor[]>{
    return this.http.get<MotionSensor[]>(this.ALL_MOTIONSENSORS_URL);
  }

  getWeatherData(): Observable<Weather[]>{
    return this.http.get<Weather[]>(this.WEATHER_URL);
  }

}
