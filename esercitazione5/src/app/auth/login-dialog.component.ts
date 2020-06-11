import { Component, OnInit } from '@angular/core';
import {AuthService} from './auth.service';
import {NgForm} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
/* https://blog.angular-university.io/angular-material-dialog/ */
@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {

  error = false;

  constructor(private auth: AuthService, dialogRef: MatDialogRef<LoginDialogComponent>) {
    auth.userLogged.subscribe(               // ascolto l'evento della login generato nel auth.service
      (user) => {
        if (user){
          dialogRef.close(true);  // chiudo il dialog se login OK -> ascolto l'evento in home.ts
        } else {
          this.error = true;                // show credenziali errate nel dialog
        }
      }
    );
  }

  ngOnInit(): void {}

  login(form: NgForm){
    this.auth.login(form.value.email, form.value.password);
  }

}
