import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ErrorDialogComponent} from './error-dialog/error-dialog.component';
import {ErrorHandler} from './error-handler';
import {ApkMaterialModule} from './apk-material/apk-material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import { LoginDialogComponent } from './login-dialog/login-dialog.component';
import {JwtHelper} from 'angular2-jwt/angular2-jwt';
import {ConfirmDialogComponent} from "./confirm-dialog/confirm-dialog.component";
import { BackButtonComponent } from './back-button/back-button.component';
import { SidenavToggleButtonComponent } from './sidenav-toggle-button/sidenav-toggle-button.component';
import { EllipsisPipe } from './ellipsis/ellipsis.pipe';
import { DateXPipe } from './date-x-pipe/date-x.pipe';


import { LaddaModule } from 'angular2-ladda';

@NgModule({
  imports: [
    CommonModule,
    ApkMaterialModule,
    FormsModule,
    LaddaModule,
    ReactiveFormsModule,
    RouterModule
  ],
  declarations: [
    ErrorDialogComponent,
    LoginDialogComponent,
    ConfirmDialogComponent,
    BackButtonComponent,
    SidenavToggleButtonComponent,
    EllipsisPipe,
    DateXPipe
  ],
  entryComponents: [
    ErrorDialogComponent,
    LoginDialogComponent,
    ConfirmDialogComponent
  ],
  exports: [
    ApkMaterialModule,
    CommonModule,
    LaddaModule,
    ReactiveFormsModule,
    RouterModule,
    BackButtonComponent,
    SidenavToggleButtonComponent,
    EllipsisPipe,
    DateXPipe
  ],
  providers: [
    ErrorHandler,
    JwtHelper
  ]

})
export class SharedModule { }
