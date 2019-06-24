import { Component, OnInit } from '@angular/core';
import {User} from '../dashboard/model/user';
import {ApiService} from '../shared/api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

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
  constructor(private  apiService: ApiService,
              private router: Router) { }

  ngOnInit() {
  }

  getUser(): void {
    this.apiService.getUser(this.userModel).subscribe(
      res => {
        // location.reload();
        if (res != null) {
          this.router.navigate(['dashboard']);
        } else {
          alert('Failed to sign in');
        }
      },
      err => {
        alert('Error in GetUser');
      }
    );
  }
}
