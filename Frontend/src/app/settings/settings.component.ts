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
    this.test();
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

  public test(){
        // Get the modal
      var modal = document.getElementById("myModal") as HTMLImageElement;;

      // Get the button that opens the modal
      var btn = document.getElementById("myBtn") as HTMLImageElement;;

      // Get the <span> element that closes the modal
      var span = document.getElementsByClassName("close")[0] as HTMLImageElement;;

      // When the user clicks the button, open the modal 
      btn.onclick = function() {
        modal.style.display = "block";
      }

      // When the user clicks on <span> (x), close the modal
      span.onclick = function() {
        modal.style.display = "none";
      }

      // When the user clicks anywhere outside of the modal, close it
      window.onclick = function(event) {
        if (event.target == modal) {
          modal.style.display = "none";
        }
      }  
  }

}