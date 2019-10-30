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

  users: User[] = [];

  constructor(private  apiService: ApiService) { }

  ngOnInit() {
    this.getUserAll();
  }

  public getUserAll() {

    this.apiService.getUserAll().subscribe(
      res => {
        this.users = res;
      },
      err => {
        //alert('Error!!!');
      }
    );
  }
}
