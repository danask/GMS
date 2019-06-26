import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/user';
import {ApiService} from '../shared/api.service';
import {Sensor} from './model/sensor';
import {MotionSensor} from './model/motionSensor';
import {Weather} from './model/weather';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

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

  constructor(private  apiService: ApiService) { }

  ngOnInit() {
    this.getLastSensor();
    this.getLastMotionSensor();
    // this.getWeatherData();
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
}
