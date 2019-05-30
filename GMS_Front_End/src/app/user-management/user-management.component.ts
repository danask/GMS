import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/user';
import {ApiService} from '../shared/api.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  // init User
  userModel: User = {
    name: '',
    email: '',
    password: '',
    phone: '',
    role: ''
  };

  // constructor(private  http: HttpClient) { }
  constructor(private  apiService: ApiService) { }

  ngOnInit() {
  }

  addUser(): void {
    // const url = 'http://localhost:8080/GMS/User/saveUser';
    // this.http.post(url, this.model).subscribe(
    this.apiService.setUser(this.userModel).subscribe(
      res => {
        location.reload();
      },
      err => {
        alert('Error in AddUser');
      }
    );
  }
}
