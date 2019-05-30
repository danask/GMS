import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../dashboard/model/user';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private BASE_URL = 'http://localhost:8080/GMS';
  private ALL_USERS_URL = this.BASE_URL + '\\User\\userAll';
  private ADD_USERS_URL = this.BASE_URL + '\\User\\saveUserApi';

  constructor(private http: HttpClient) {

  }

  getUserAll(): Observable<User[]>{
    return this.http.get<User[]>(this.ALL_USERS_URL);
  }

  setUser(user: User): Observable<any>{
    return this.http.post(this.ADD_USERS_URL, user);
  }
}
