import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dashboard/model/user';
import {Sensor} from '../dashboard/model/sensor';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private BASE_URL = 'http://localhost:8080/GMS';
  private ALL_USERS_URL = this.BASE_URL + '\\User\\userAll';
  private ADD_USERS_URL = this.BASE_URL + '\\User\\saveUserApi';

  private ALL_SENSORS_URL = this.BASE_URL + '\\Sensor\\sensorAll';

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
}
