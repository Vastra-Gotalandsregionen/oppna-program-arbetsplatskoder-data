import { Component } from '@angular/core';
import {StateService} from './core/state/state.service';
import {MatDialog, MatDialogRef, MatDialogConfig} from '@angular/material';
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
              private dialog: MatDialog,
              private router: Router) {}

  openLogin() {

    const dialogConfig:MatDialogConfig = {
      disableClose: false,
      hasBackdrop: true,
      panelClass: 'apk-dialog'
    };

    const dialogRef: MatDialogRef<LoginDialogComponent> = this.dialog.open(LoginDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
    });
  }

  logout() {
    this.authService.resetAuth();
    this.router.navigate(['/']);
  }

  getShowContentEdit(): boolean {
    return this.stateService.showContentEdit;
  }

  setShowContentEdit(value : boolean) {
    this.stateService.showContentEdit = value;
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

  isLoggedIn() {
    return this.authService.isAuthenticated();
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
    return this.authService.isAdmin();
  }
}
