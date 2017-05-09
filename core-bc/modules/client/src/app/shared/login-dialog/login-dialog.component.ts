import {Component, OnInit, ViewChild} from '@angular/core';
import {MdDialogRef} from '@angular/material';
import {NgForm} from '@angular/forms';
import {StateService} from '../../core/state/state.service';
import 'rxjs/add/operator/timeout';
import {TimeoutError} from 'rxjs/Rx';
import {AuthService} from '../../core/auth/auth.service';
import {JwtHttp} from '../../core/jwt-http';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.scss']
})
export class LoginDialogComponent implements OnInit {

  public dialogRef: MdDialogRef<LoginDialogComponent>;

  @ViewChild(NgForm) form: NgForm;

  userId: string;
  password: string;

  loginMessage: string;

  constructor(dialogRef: MdDialogRef<LoginDialogComponent>,
              private http: JwtHttp,
              private stateService: StateService,
              private authService: AuthService) {
    this.dialogRef = dialogRef;
  }

  ngOnInit() {
  }

  login() {

    if (this.form.valid) {
      this.stateService.startShowProgress();
      this.http.post('/api/login', {username: this.userId, password: this.password})
        .timeout(10000)
        .finally(() => this.stateService.stopShowProgress())
        .subscribe(response => {
          console.log(response);

          // this.location.navigate(['/']);

          this.authService.jwt = response.text();

          this.dialogRef.close(response);
        }, error => {
          if (Object.getPrototypeOf(error) === Object.getPrototypeOf(new TimeoutError())) {
            this.loginMessage = 'Tidsgränsen för anropet gick ut.'
          } else if (error.status && error.status >= 400 && error.status < 500) {
            this.loginMessage = 'Felaktiga inloggningsuppgifter';
          } else {
            this.loginMessage = 'Tekniskt fel';
          }

          console.log(error);
        });
    }
  }


}
