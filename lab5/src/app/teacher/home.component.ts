import {Component, OnInit} from '@angular/core';
import {LoginDialogComponent} from '../auth/login-dialog.component';
import {AuthService} from '../auth/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-home',
  template: '<h1>Home page</h1>',
  styles: ['*{text-align: center; padding: 50px;}']
})
export class HomeComponent implements OnInit {

  constructor(private auth: AuthService, private router: Router, public dialog: MatDialog, private activeRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activeRoute.queryParamMap
      .subscribe(params => {
        if (params.has('doLogin') && params.get('doLogin') === 'true') {
          this.dialog.open(LoginDialogComponent, {disableClose: true})
            .afterClosed()                 // dopo chiusura del dialog => redirect
            .subscribe(result => {
              if (result) {                // se result = false => click su CANCEL
                const url = this.auth.getUrl();
                if (url != null){
                  this.auth.deleteUrl();
                  this.router.navigateByUrl(url);
                } else {
                  this.router.navigate(['home']);
                }
              } else {
                this.router.navigate(['home']);
              }
            });
        }
      });
  }

}
