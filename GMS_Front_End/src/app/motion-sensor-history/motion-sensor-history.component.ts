import { Component, OnInit } from '@angular/core';
import {Sensor} from '../dashboard/model/sensor';
import {MotionSensor} from '../dashboard/model/motionSensor';
import {ApiService} from '../shared/api.service';

@Component({
  selector: 'app-motion-sensor-history',
  templateUrl: './motion-sensor-history.component.html',
  styleUrls: ['./motion-sensor-history.component.css']
})
export class MotionSensorHistoryComponent implements OnInit {

  motionSensors: MotionSensor[] = [];

  constructor(private  apiService: ApiService) { }

  ngOnInit() {
    this.getMotionSensorAll();
  }


  public getMotionSensorAll() {

    this.apiService.getMotionSensorAll().subscribe(
      res => {
        this.motionSensors = res;
      },
      err => {
        // alert('Error!!!');
      }
    );
  }
}
