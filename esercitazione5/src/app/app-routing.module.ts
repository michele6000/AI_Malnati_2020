import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './teacher/home.component';
import { StudentsContComponent } from './teacher/students-cont.component';
import { VmsContComponent } from './teacher/vms-cont.component';
import { PageNotFoundComponent } from './teacher/page-not-found.component';
import { RouteGuard } from './auth/route.guard';


const routes: Routes = [
  { path: 'homepage', component: HomeComponent } ,
  {path: 'teacher/course', children: [
      {
        path: 'applicazioni-internet/students',
        component: StudentsContComponent,
        canActivate: [RouteGuard]
      }
    ]},
  {path: 'teacher/course', children: [
      {
        path: 'applicazioni-internet/vms',
        component: VmsContComponent,
        canActivate: [RouteGuard]
      }
    ]},
  {path: '**', component: PageNotFoundComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: false} )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
