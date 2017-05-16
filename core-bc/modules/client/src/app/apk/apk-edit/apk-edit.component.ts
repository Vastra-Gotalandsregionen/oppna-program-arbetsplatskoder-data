import { Component, OnInit } from '@angular/core';
import {Location} from "@angular/common";
import {ApkDetailComponent} from '../apk-detail/apk-detail.component';
import {Http} from '@angular/http';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../core/auth/auth.service';

@Component({
  selector: 'app-apk-edit',
  templateUrl: './apk-edit.component.html',
  styleUrls: ['./apk-edit.component.css']
})
export class ApkEditComponent extends ApkDetailComponent implements OnInit {

  location: Location;

  constructor(route: ActivatedRoute,
              http: Http,
              location: Location,
              authService: AuthService) {
    super(route, http, location, authService);
    this.location = location;
  }

  ngOnInit() {
    super.ngOnInit();
  }

  get dataId() {
    return super.getId();
  }
}
