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
  imageList: any[];
  rowIndexArray: any[];

  private url;

  constructor(private  apiService: ApiService) {
    this.getMediaURL('/CapturedImages/2019-07-09 00:13:32.549379.jpg');
  }

  ngOnInit() {
    this.getMotionSensorAll();
    // this.apiService.imageDetailList.snapshotChanges().subscribe(
    //   list => {
    //     this.imageList = list.map(item => { return item.payload.val(); });
    //     this.rowIndexArray = Array.from(Array(Math.ceil(this.imageList.length / 3)).keys());
    //
    //     console.log(this.imageList[2]);
    //     console.log(this.rowIndexArray);
    //   }
    // );
  }

  private getMediaURL(imageRef) {
    const that = this;
    this.apiService.getMediaURL(imageRef).then(url => that.url = url);
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
