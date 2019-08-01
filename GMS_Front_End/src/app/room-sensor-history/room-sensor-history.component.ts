import { Component, OnInit } from '@angular/core';
import {Sensor} from '../dashboard/model/sensor';
import {MotionSensor} from '../dashboard/model/motionSensor';
import {ApiService} from '../shared/api.service';
import {Chart} from 'chart.js';
import {Criteria} from '../dashboard/model/criteria';

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
  waterData = [];
  dateData = [];
  dateLabel = 'date';
  criteria: Criteria;
  criteriaWater;
  
  constructor(private  apiService: ApiService) { }

  ngOnInit() {

    this.getCriteria();
    this.getSensorAll();



    // disiplay charts
    new Chart('canvasTemperature', {
      type: 'line',
      data: {
        labels: this.dateData,
        datasets: [
          {
            label: 'Temperature',
            data: this.tempData,
            // backgraoundColor: 'rgb(255,99,132)',
            borderColor: 'rgb(255,99,132)',
            fill: false,
          },
          {
            label: 'Humidity',
            data: this.humidityData,
            // backgraoundColor: 'rgb(54,162,235)',
            borderColor: 'rgb(54,162,235)',
            fill: false,
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
          }
          ],
          yAxes: [{
            display: true,
            scaleLabel:{
              // id: 'y-axis-1',
              display: true,
              // stacked: true,
              // position: 'right',
              labelString: 'degree(C) / percent(%)'
            }
          }]
          // yAxes: [{
          //   display: true,
          //   scaleLabels:[{
          //     id: 'y-axis-1',
          //     display: true,
          //     position: 'right',
          //     labelString: 'degree'
          //   },
          //   {
          //     id: 'y-axis-2',
          //     display: true,
          //     position: 'right',
          //     labelString: 'degree2'
          //   }]
           
          // }]
        }
      }
    });

    // this.chartHumidity = new Chart('canvasHumidity', {
    //   type: 'line',
    //   data: {
    //     labels: this.dateData,
    //     datasets: [
    //       {
    //         label: 'Humidity',
    //         data: this.humidityData,
    //         backgraoundColor: 'rgb(54,162,235)',
    //         borderColor: 'rgb(54,162,235)',
    //         fill: true,
    //       }
    //     ]
    //   },
    //   options: {
    //     responsive: true,
    //     title: {
    //       display: false,
    //       text: 'Humidity'
    //     },
    //     tooltips: {
    //       mode: 'index',
    //       intersect: false,
    //     },
    //     hover: {
    //       mode: 'nearest',
    //       intersect: true
    //     },
    //     scales: {
    //       xAxes: [{
    //         display: true,
    //         scaleLabel: {
    //           display: true,
    //           labelString: this.dateLabel
    //         }
    //       }],
    //       yAxes: [{
    //         display: true,
    //         scaleLabel: {
    //           display: true,
    //           labelString: 'Percent (%)'
    //         }
    //       }]
    //     }
    //   }
    // });


    // disiplay water charts
    new Chart('canvasWater', {
      type: 'line',
      data: {
        labels: this.dateData,
        datasets: [
          {
            label: 'Amount of Water',
            data: this.waterData,
            // backgraoundColor: 'rgb(160,19,194)',
            borderColor: 'rgb(160,19,194)',
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
              labelString: 'liters (l)'
            },
            ticks: {
              min: 1.0,
              
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

            // 1: 2 = x : 1.8
            this.sensors[i].description = 
              (parseFloat(this.sensors[i].description)*this.criteriaWater*0.5).toFixed(3);

            this.waterData[size -1 - i] = this.sensors[i].description;
            this.dateData[size -1 - i] = this.sensors[i].dateTime.substring(5,10);
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

  public getCriteria()
  {
    this.apiService.getCriteria().subscribe(
      res => {
        this.criteria = res;
        this.criteriaWater = parseFloat(this.criteria.criteriaWater.toString()) / 1000;

        this.criteria.criteriaWater = 
              (this.criteriaWater).toFixed(3).toString();
      },
      err => {
        // alert('Error!!!');
      }
    );    
  }
    
  public refresh(): void {
    window.location.reload();
  }


}
