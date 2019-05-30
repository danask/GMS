import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/user';
import {ApiService} from '../shared/api.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

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
        alert('Error!!!');
      }
    );
  }

}
