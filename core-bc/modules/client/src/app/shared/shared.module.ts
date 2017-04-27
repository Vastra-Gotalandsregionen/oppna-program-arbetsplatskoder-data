import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ErrorDialogComponent} from "./error-dialog/error-dialog.component";
import {ErrorHandler} from "./error-handler";
import {ApkMaterialModule} from "./apk-material/apk-material.module";
import {ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";

@NgModule({
  imports: [
    CommonModule,
    ApkMaterialModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  declarations: [
    ErrorDialogComponent
  ],
  entryComponents: [
    ErrorDialogComponent
  ],
  exports: [
    ApkMaterialModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  providers: [
    ErrorHandler
  ]

})
export class SharedModule { }
