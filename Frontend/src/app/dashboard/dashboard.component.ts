import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/user';
import {ApiService} from '../shared/api.service';
import {Sensor} from './model/sensor';
import {MotionSensor} from './model/motionSensor';
import {Criteria} from './model/criteria';

import {Weather} from './model/weather';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AngularFireStorage} from '@angular/fire/storage';
import {finalize} from 'rxjs/operators';
import { formatDate } from '@angular/common';
import { Status } from './model/status';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  imgSrc: String = '/assets/img/default_statistics.png';
  selectedImage: any = null;
  isSubmitted: boolean

  private url;
  private tempMediaURL;

  formTemplate = new FormGroup({
    caption: new FormControl('', Validators.required),
    category: new FormControl(''),
    imageUrl: new FormControl('',  Validators.required)
  })

  sensor: Sensor;
  motionSensor: MotionSensor;
  criteria: Criteria;
  status: Status;

  //weatherInfoEntity: Weather;
  countryUrl = '/assets/img/canada.png';
  weather = "Clouds";
  weatherDescription = '';
  weatherUrl = '/assets/img/sunny.png';
  humidity = '';
  temp = '';
  temperatureUrl = '/assets/img/temperature.png';
  temp_min = '';
  temp_max = '';
  wind = '';
  pressure = '';
  currentDate = '';

  temp_gap = '0';

  constructor(private  apiService: ApiService,
              private  storage: AngularFireStorage
              ) 
  {

  }

  ngOnInit() {
    this.getLastSensor();
    this.getLastMotionSensor();
    this.getWeatherData();
    this.getCriteria();
    this.temp = "6.44";
    this.temp_min = "4.44";
    this.temp_max = "8.33";
    this.weather = "Clouds";
    this.weatherDescription = "broken clouds";
    this.humidity = "87";
    this.pressure = "1022";
    this.wind = "1.5";

    console.log(this.weather);

    if(parseInt(this.temp) >= 30)
      this.temperatureUrl = '/assets/img/temperature_hot.png';

    if(this.weather == 'Clouds' || this.weather == 'clouds')
      this.weatherUrl = '/assets/img/cloud.png';
    else if(this.weather == 'Clear')
      this.weatherUrl = '/assets/img/sunny.png';
    else if(this.weather == 'Snow')
      this.weatherUrl = '/assets/img/snowman.png';
    else
      this.weatherUrl = '/assets/img/umbrella.png';

    this.resetForm();
    this.apiService.getImageDetailList();
    this.currentDate = formatDate(new Date(), 'yyyy/MM/dd', 'en').toString();

    this.imageModal();

    // var tempGap = parseInt(this.criteria.criteriaTemperature.toString()) - 
    //               parseInt(this.sensor.sensorTemp.toString());

    // this.status.temperature = tempGap.toString();
    // this.temp_gap = tempGap.toString();
  }

  private getMediaURL(imageRef) {
    const that = this;
    this.apiService.getMediaURL(imageRef).then(url => that.url = url);
  }

  public getCriteria()
  {
    this.apiService.getCriteria().subscribe(
      res => {
        this.criteria = res;
        var criteriaWater = parseFloat(this.criteria.criteriaWater.toString()) / 1000;
        
        console.log("temp: " + this.temp);
        // to calculate how long sunshine should be lasted
        this.criteria.criteriaSunshine = 
              ((parseFloat(this.temp) / 25.0) * 60).toFixed(2).toString(); 
        this.criteria.criteriaWater = 
              (criteriaWater).toFixed(2).toString();

        // 1: 2 = x : 1.8
        this.sensor.description = 
              (parseFloat(this.sensor.description)*criteriaWater*0.5).toFixed(2);
      },
      err => {
        // alert('Error!!!');
      }
    );    
  }

  public getLastSensor() {

    this.apiService.getLastSensor().subscribe(
      res => {
        this.sensor = res;
      },
      err => {
        // alert('Error!!!');
      }
    );
  }

  public getLastMotionSensor() {

    this.apiService.getLastMotionSensor().subscribe(
      res => {
        this.motionSensor = res;

        // this.tempMediaURL = '/CapturedImages/' + this.motionSensor.detectTime + '.jpg';
        // console.log(this.tempMediaURL);
        this.getMediaURL('/CapturedImages/' + this.motionSensor.detectTime + '.jpg');

        this.motionSensor.detectTime = this.motionSensor.detectTime.substring(0, 19);
        // this.getMediaURL('/CapturedImages/2019-07-09 00:13:40.180848.jpg');
      },
      err => {
        // alert('Error!!!');
      }
    );
  }


  public getWeatherData() {
    this.apiService.getWeatherData().subscribe(
      res => {
        // this.weathers = res;
        const weatherInfo = res;

        if (weatherInfo != null) {
          console.log(res);
          // console.log(res.main.temp);
          // console.log(weatherInfo.main.humidity);

          // this.weather = res.weather[0].main;
          // this.humidity = weatherInfo.main.humidity;
          // this.temp = weatherInfo.main.temp;
          // this.weatherDescription = res.weather[0].description;
          // this.temp_min = weatherInfo.main.temp_min;
          // this.temp_max = weatherInfo.main.temp_max;
          // this.wind = res.wind.speed;
          // this.pressure = weatherInfo.main.pressure;

          console.log(this.weather);
        }
      },
      err => {
        // alert('Error!!!');
      }
    );


  }



  showPreview(event:any)
  {
    if (event.target.files && event.target.files[0])
    {
      const reader = new FileReader();
      reader.onload = (e:any) => this.imgSrc = e.target.result;
      reader.readAsDataURL(event.target.files[0]);
      this.selectedImage = event.target.files[0];
    }
    else
    {
      this.imgSrc = '/assets/img/sprout.png';
      this.selectedImage = null;
    }
  }


  onSubmit(formValue){
    this.isSubmitted = true;

    if (this.formTemplate.valid) {
      var filePath = `${formValue.category}/${this.selectedImage.name}_${new Date().getTime()}`;
    // .split('.').slice(0, -1).join('.')

      const fileRef = this.storage.ref(filePath);
      this.storage.upload(filePath, this.selectedImage).snapshotChanges().pipe(
        finalize(() => {
          fileRef.getDownloadURL().subscribe((url) => {
            formValue['imageUrl'] = url;
            this.apiService.insertImageDetails(formValue);
            this.resetForm();
          });
        })
      ).subscribe();
    }
  }

  get formControls()
  {
    return this.formTemplate['controls'];
  }

  resetForm(){
    this.formTemplate.reset();
    this.formTemplate.setValue({
      caption: '',
      imageUrl: '',
      category: 'type'
    });
    this.imgSrc = '/assets/img/sprout.png';
    this.selectedImage = null;
    this.isSubmitted = false;

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
