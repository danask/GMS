import { Component, OnInit } from '@angular/core';
import {Sensor} from '../dashboard/model/sensor';
import {MotionSensor} from '../dashboard/model/motionSensor';
import {ApiService} from '../shared/api.service';
import {AngularFireStorage} from '@angular/fire/storage';

@Component({
  selector: 'app-motion-sensor-history',
  templateUrl: './motion-sensor-history.component.html',
  styleUrls: ['./motion-sensor-history.component.css']
})
export class MotionSensorHistoryComponent implements OnInit {

  motionSensors: MotionSensor[] = [];
  imageList: any[];
  rowIndexArray: any[];
  capturedImageURL: any[];

  private url;

  constructor(private  apiService: ApiService,
              private  storage: AngularFireStorage) {

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

        for (let i = 0; i < this.motionSensors.length; i++ ){
          this.apiService.getMediaURL('/CapturedImages/' + this.motionSensors[i].detectTime + '.jpg').
          then(url => this.motionSensors[i].pirId = url);

          if (this.url == null) {
            this.motionSensors[i].pirId = '/assets/img/default_statistics.png';
          }
        }
      },
      err => {
        // alert('Error!!!');
      }
    );
  }
}
