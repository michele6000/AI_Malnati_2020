import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from './teacher/home.component';
import {StudentsContComponent} from './teacher/students-cont.component';
import {VmsContComponent} from './teacher/vms-cont.component';
import {PageNotFoundComponent} from './teacher/page-not-found.component';
import {RouteGuard} from './route.guard';


const routes: Routes = [
  // Home Page
  {path: 'home', component: HomeComponent},
  // Course/Students Page
  {path: 'teacher/course/applicazioni-internet/students', component: StudentsContComponent, canActivate: [RouteGuard]},
  // Vms Page
  {path: 'teacher/course/applicazioni-internet/vms', component: VmsContComponent, canActivate: [RouteGuard]},
  // Page not found view
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: false} )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
