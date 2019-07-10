import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/user';
import {ApiService} from '../shared/api.service';
import {Sensor} from './model/sensor';
import {MotionSensor} from './model/motionSensor';
import {Weather} from './model/weather';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AngularFireStorage} from '@angular/fire/storage';
import {finalize} from 'rxjs/operators';


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

  //weatherInfoEntity: Weather;
  weather = '';
  weatherDescription = '';
  humidity = '';
  temp = '';
  temp_min = '';
  temp_max = '';
  wind = '';
  pressure = '';

  constructor(private  apiService: ApiService,
              private  storage: AngularFireStorage) {

  }

  ngOnInit() {
    this.getLastSensor();
    this.getLastMotionSensor();
    // this.getWeatherData();
    this.resetForm();
    this.apiService.getImageDetailList();

  }

  private getMediaURL(imageRef) {
    const that = this;
    this.apiService.getMediaURL(imageRef).then(url => that.url = url);
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
          // console.log(res);
          // console.log(weatherInfo.main.temp);
          // console.log(weatherInfo.main.humidity);
          // console.log(res.weather[0].main);

          // this.weather = res.weather[0].main;
          // this.humidity = weatherInfo.main.humidity;
          // this.temp = (weatherInfo.main.temp - 32) / 1.8;
          // this.weatherDescription = res.weather[0].description;
          // this.temp_min = weatherInfo.main.temp_min;
          // this.temp_max = weatherInfo.main.temp_max;
          // this.wind = res.wind.speed;
          // this.pressure = weatherInfo.main.pressure;

          // this.weatherInfoEntity.humidity = weatherInfo.main.humidity;
          // this.weatherInfoEntity.pressure = weatherInfo.main.pressure;
          // this.weatherInfoEntity.temp = weatherInfo.main.temp;
          // this.weatherInfoEntity.temp_max = weatherInfo.main.temp_max;
          // this.weatherInfoEntity.temp_min = weatherInfo.main.temp_min;
          // this.weatherInfoEntity.weather = res.weather[0].main;
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
      this.imgSrc = '/assets/img/default_statistics.png';
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
    this.imgSrc = '/assets/img/default_statistics.png';
    this.selectedImage = null;
    this.isSubmitted = false;

  }

}
