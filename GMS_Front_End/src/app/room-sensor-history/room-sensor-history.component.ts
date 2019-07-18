import { Component, OnInit } from '@angular/core';
import {Sensor} from '../dashboard/model/sensor';
import {MotionSensor} from '../dashboard/model/motionSensor';
import {ApiService} from '../shared/api.service';
import {Chart} from 'chart.js';

@Component({
  selector: 'app-room-sensor-history',
  templateUrl: './room-sensor-history.component.html',
  styleUrls: ['./room-sensor-history.component.css']
})
export class RoomSensorHistoryComponent implements OnInit {

  sensors: Sensor[] = [];
  motionSensors: MotionSensor[] = [];
  title = 'graph';
  chartTemperature = [];
  chartHumidity = [];
  tempData = [];
  humidityData = [];
  dateData = [];
  dateLabel = 'date';

  constructor(private  apiService: ApiService) { }

  ngOnInit() {

    this.getSensorAll();

    // disiplay charts
    this.chartTemperature = new Chart('canvasTemperature', {
      type: 'line',
      data: {
        labels: this.dateData,
        datasets: [
          {
            label: 'Temperature',
            data: this.tempData,
            backgraoundColor: 'rgb(255,99,132)',
            borderColor: 'rgb(255,99,132)',
            fill: true,
          }
        ]
      },
      options: {
        responsive: true,
        title: {
          display: false,
          text: 'Temperature'
        },
        tooltips: {
          mode: 'index',
          intersect: false,
        },
        hover: {
          mode: 'nearest',
          intersect: true
        },
        scales: {
          xAxes: [{
            display: true,
            scaleLabel: {
              display: true,
              labelString: this.dateLabel
            }
          }],
          yAxes: [{
            display: true,
            scaleLabel: {
              display: true,
              labelString: 'degree'
            }
          }]
        }
      }
    });

    this.chartHumidity = new Chart('canvasHumidity', {
      type: 'line',
      data: {
        labels: this.dateData,
        datasets: [
          {
            label: 'Humidity',
            data: this.humidityData,
            backgraoundColor: 'rgb(54,162,235)',
            borderColor: 'rgb(54,162,235)',
            fill: true,
          }
        ]
      },
      options: {
        responsive: true,
        title: {
          display: false,
          text: 'Humidity'
        },
        tooltips: {
          mode: 'index',
          intersect: false,
        },
        hover: {
          mode: 'nearest',
          intersect: true
        },
        scales: {
          xAxes: [{
            display: true,
            scaleLabel: {
              display: true,
              labelString: this.dateLabel
            }
          }],
          yAxes: [{
            display: true,
            scaleLabel: {
              display: true,
              labelString: 'degree'
            }
          }]
        }
      }
    });

  }

  public getSensorAll() {

    this.apiService.getSensorAll().subscribe(
      res => {
        this.sensors = res;

        var size = this.sensors.length;
        
        if(this.sensors.length > size)
          size = 9;

        // last 10 elements
        console.log(size);
        for(let i = size - 1; i >= 0; i--)
        {
          if(this.sensors[i] != null)
          {
            this.tempData[size -1 - i] = this.sensors[i].sensorTemp;
            this.humidityData[size -1 - i] = this.sensors[i].sensorHumid;
            this.dateData[size -1 - i] = this.sensors[i].dateTime.substring(8,10);
            this.dateLabel = 'date (' + this.sensors[i].dateTime.substring(5,7) + ')';
            console.log(this.dateLabel);
          }
        }

      },
      err => {
        // alert('Error!!!');
      }
    );
  }

}
