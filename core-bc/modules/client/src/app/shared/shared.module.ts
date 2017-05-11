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

@NgModule({
  imports: [
    CommonModule,
    ApkMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ],
  declarations: [
    ErrorDialogComponent,
    LoginDialogComponent,
    ConfirmDialogComponent
  ],
  entryComponents: [
    ErrorDialogComponent,
    LoginDialogComponent,
    ConfirmDialogComponent
  ],
  exports: [
    ApkMaterialModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  providers: [
    ErrorHandler,
    JwtHelper
  ]

})
export class SharedModule { }
