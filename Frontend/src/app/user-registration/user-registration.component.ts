import { Component, OnInit } from '@angular/core';
import {User} from '../user-management/model/user';
import {ApiService} from '../shared/api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  // init User
  userModel: User = {
    id: 0,
    name: '',
    email: '',
    password: '',
    phone: '',
    role: ''
  };

  // constructor(private  http: HttpClient) { }
  constructor(private apiService: ApiService,
              private router: Router) { }

  ngOnInit() {
  }

  addUser(): void {
    // const url = 'http://localhost:8080/GMS/User/saveUser';
    // this.http.post(url, this.model).subscribe(
    this.apiService.setUser(this.userModel).subscribe(
      res => {
        //location.reload();
        if (res != null) {
          this.router.navigate(['logout']);
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
