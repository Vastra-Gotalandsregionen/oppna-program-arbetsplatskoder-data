import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ApkComponent } from './apk/apk.component';
import { ApkDetailComponent } from './apk-detail/apk-detail.component';
import { ApkEditComponent } from './apk-edit/apk-edit.component';
import { ApkCreateComponent } from './apk-create/apk-create.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ApkMaterialModule} from "./apk-material/apk-material.module";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ApkComponent,
    ApkDetailComponent,
    ApkEditComponent,
    ApkCreateComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    // FormsModule,
    HttpModule,
    AppRoutingModule,
    ApkMaterialModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
