import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material/sidenav';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AuthService} from './auth/auth.service';

/*
* CERTIFICATO
* git clone https://github.com/RubenVermeulen/generate-trusted-ssl-certificate.git
* bash generate.sh
* */

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  title = 'lab5';

  @ViewChild('sidenav') sidenav: MatSidenav;


  isUserLoggedIn = false;
  email = '';

  constructor(private auth: AuthService, public dialog: MatDialog, private router: Router) {
    // mi iscrivo all'observable dell'auth.service per accedere alla mail e al boolean in seguito alla login/logout
    this.auth.user.subscribe(
      result => {
        if (result != null){
          // login
          this.isUserLoggedIn = result.isLogged;
          this.email = result.email;
        } else {
          // logout
          this.isUserLoggedIn = false;
          this.email = '';
          router.navigate(['home']);
        }
      }
    );
  }

  ngOnInit() {
  }

  toggleForMenuClick(){
    this.sidenav.opened ? this.sidenav.close() : this.sidenav.open();
  }

  logout(){
    this.auth.logout();
  }


}
