import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth.service';
import { NgForm } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
/* https://blog.angular-university.io/angular-material-dialog/ */
@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {
  error = false;

  constructor(
    private auth: AuthService,
    dialogRef: MatDialogRef<LoginDialogComponent>
  ) {
    auth.userLogged.subscribe(
      user => {
        if (user) {
          dialogRef.close(true);
        } else {
          this.error = true;
        }
      }
    );
  }

  ngOnInit(): void {}

  login(form: NgForm) {
    this.auth.login(form.value.email, form.value.password);
  }
}
