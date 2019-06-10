import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/user';
import {ApiService} from '../shared/api.service';
import {Sensor} from './model/sensor';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  sensors: Sensor[] = [];

  constructor(private  apiService: ApiService) { }

  ngOnInit() {
    this.getSensorAll();
  }

  public getSensorAll() {

    this.apiService.getSensorAll().subscribe(
      res => {
        this.sensors = res;
      },
      err => {
        //alert('Error!!!');
      }
    );
  }

}
