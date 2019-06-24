import { Component, OnInit } from '@angular/core';
import {Sensor} from '../dashboard/model/sensor';
import {MotionSensor} from '../dashboard/model/motionSensor';
import {ApiService} from '../shared/api.service';

@Component({
  selector: 'app-room-sensor-history',
  templateUrl: './room-sensor-history.component.html',
  styleUrls: ['./room-sensor-history.component.css']
})
export class RoomSensorHistoryComponent implements OnInit {

  sensors: Sensor[] = [];
  motionSensors: MotionSensor[] = [];

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
        // alert('Error!!!');
      }
    );
  }

}
