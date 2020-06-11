import { Component, OnInit } from '@angular/core';
import { LoginDialogComponent } from '../auth/login-dialog.component';
import { AuthService } from '../auth/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-home-component',
  template: `
    <h1 style="text-align: center ; padding: 40px ; font-style: italic ; font-size: 25px">Welcome back on VirtLabs</h1>
  `,
  styles: ['h1 { font-weight: normal;  }']
})
export class HomeComponent implements OnInit {

  constructor(private auth: AuthService, private router: Router, public dialog: MatDialog, private activeRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activeRoute.queryParamMap
      .subscribe(params => {
        if (params.has('doLogin') && params.get('doLogin') === 'true') {
          this.dialog.open(LoginDialogComponent, {disableClose: true})
            .afterClosed()              // dopo la chiusura del dialog faccio le redirect
            .subscribe(result => {
              if (result) {      // se result è false => click su CANCEL
                const myurl = this.auth.getUrl();
                if (myurl != null){
                  this.auth.deleteUrl();
                  this.router.navigateByUrl(myurl);
                } else {
                  this.router.navigate(['homepage']);
                }
              } else {
                this.router.navigate(['homepage']);
              }
            });
        }
      });
  }

}
