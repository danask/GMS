import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AngularFireModule } from '@angular/fire';
import { AngularFireStorageModule } from '@angular/fire/storage';
import { AngularFireDatabaseModule} from '@angular/fire/database';
import { ReactiveFormsModule } from "@angular/forms";
import { environment } from '../environments/environment';


import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { LogoutComponent } from './logout/logout.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { Router, RouterModule, Routes} from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { UserManagementComponent } from './user-management/user-management.component';
import { UserRegistrationComponent } from './user-registration/user-registration.component';
import { MotionSensorHistoryComponent } from './motion-sensor-history/motion-sensor-history.component';
import { RoomSensorHistoryComponent } from './room-sensor-history/room-sensor-history.component';
import { SettingsComponent } from './settings/settings.component';



const appRoutes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  {
    path: 'motionSensorHistory',
    component: MotionSensorHistoryComponent
  },
  {
    path: 'roomSensorHistory',
    component: RoomSensorHistoryComponent
  },
  {
    path: 'userManagement',
    component: UserManagementComponent
  },
  {
    path: 'userRegistration',
    component: UserRegistrationComponent
  },
  {
    path: 'logout',
    component: LogoutComponent
  },
  {
    path: 'settings',
    component: SettingsComponent
  },
  // default active menu
  {
    path: '',
    component: LogoutComponent,//DashboardComponent,
    pathMatch: 'full'
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];


@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    LogoutComponent,
    DashboardComponent,
    NotFoundComponent,
    UserManagementComponent,
    UserRegistrationComponent,
    MotionSensorHistoryComponent,
    RoomSensorHistoryComponent,
    SettingsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFireStorageModule,
    AngularFireDatabaseModule,
    ReactiveFormsModule,

    RouterModule.forRoot(appRoutes, {enableTracing: true})
    // RouterModule.forRoot([
    //   {path: 'logout', component: LogoutComponent}]
    // )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
