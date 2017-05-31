import { Component } from '@angular/core';
import {StateService} from './core/state/state.service';
import {MdDialog, MdDialogRef, MdDialogConfig} from '@angular/material';
import {LoginDialogComponent} from './shared/login-dialog/login-dialog.component';
import {AuthService} from './core/auth/auth.service';
import {Router} from '@angular/router';
import {DomSanitizer, SafeStyle} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private authService: AuthService,
              private stateService: StateService,
              private sanitizer: DomSanitizer,
              private dialog: MdDialog,
              private router: Router) {}

  openLogin() {

    const dialogConfig:MdDialogConfig = {
      disableClose: false,
      hasBackdrop: true
      //,panelClass: ''
    };

    const dialogRef: MdDialogRef<LoginDialogComponent> = this.dialog.open(LoginDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
    });
  }

  logout() {
    this.authService.resetAuth();
    this.router.navigate(['/']);
  }

  getShowDebug(): boolean {
    return this.stateService.showDebug;
  }

  setShowDebug(value : boolean) {
    this.stateService.showDebug = value;
  }

  getLoggedInDisplayName() {
    return this.authService.getLoggedInDisplayName();
  }

  getLoggedInUserId(): string {
    return this.authService.getLoggedInUserId();
  }

  userAvatarBackgroundImageStyle(): SafeStyle {
    return this.sanitizer.bypassSecurityTrustStyle('url(/api/user/' + this.getLoggedInUserId() + '/thumbnailPhoto)');
  }

  get showProgress() {
    return this.stateService.showProgress;
  }

  get admin() {
    return this.authService.getLoggedInRole() === 'ADMIN';
  }
}
