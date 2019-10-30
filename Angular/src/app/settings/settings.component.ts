import { Component, OnInit } from '@angular/core';
import {Criteria} from '../dashboard/model/criteria';
import {ApiService} from '../shared/api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {


  // init Criteria
  criteria: Criteria;

  // constructor(private  http: HttpClient) { }
  constructor(private apiService: ApiService,
              private router: Router) { }

  ngOnInit() {
    this.getCriteria();
  }

  public getCriteria()
  {
    this.apiService.getCriteria().subscribe(
      res => {
        this.criteria = res;

        if(this.criteria.emailNotification != "off")
          this.criteria.emailNotification = "on";
      },
      err => {
        // alert('Error!!!');
      }
    );    
  }

  addCriteria(): void {

    this.apiService.setCriteria(this.criteria).subscribe(
      res => {
        //location.reload();
        if (res != null) {
          this.router.navigate(['dashboard']);
        } else {
          alert('Failed to add');
        }
      },
      err => {
        //alert('Error in AddUser');
      }
    );
  }
}