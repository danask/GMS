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
    this.imageModal();

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

        for (let i = 0; i < this.motionSensors.length; i++ )
        {
          this.apiService.getMediaURL('/CapturedImages/' + this.motionSensors[i].detectTime + '.jpg').
          then(url => this.motionSensors[i].pirId = url);

          if (this.url == null) {
            this.motionSensors[i].pirId = '/assets/img/sprout.png';
          }

          this.motionSensors[i].detectTime = this.motionSensors[i].detectTime.substring(0, 19);
        }
      },
      err => {
        // alert('Error!!!');
      }
    );
  }
      
  public refresh(): void {
    window.location.reload();
  }

  public imageModal()
  {

    // Get the modal
    var modal = document.getElementById("myModal");

    // Get the image and insert it inside the modal - use its "alt" text as a caption
    var img = document.getElementById("myImg") as HTMLImageElement;
    var modalImg = document.getElementById("img01") as HTMLImageElement;
    var captionText = document.getElementById("caption") as HTMLImageElement;

    img.onclick = function(){
      modal.style.display = "block";
      modalImg.src = img.src;
      captionText.innerHTML = img.alt;
    }

    var span = document.getElementsByClassName("close")[0] as HTMLImageElement;
   span.onclick = function(){
    modal.style.display = "none";
   } 
  }
}
