import { Component, OnInit } from '@angular/core';
import {User} from '../dashboard/model/user';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  // addUser(): void {
  //   const url = 'http://localhost:8080/GMS/User/saveUser';
  //   this.http.post(url, this.model).subscribe(
  //     res => {
  //       location.reload();
  //     },
  //     err => {
  //       alert('Error in AddUser');
  //     }
  //   );
}
