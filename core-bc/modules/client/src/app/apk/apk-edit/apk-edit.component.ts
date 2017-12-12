import { Component, OnInit, ViewChild } from '@angular/core';
import {ApkDetailComponent} from '../apk-detail/apk-detail.component';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../core/auth/auth.service';
import {JwtHttp} from "../../core/jwt-http";

@Component({
  selector: 'app-apk-edit',
  templateUrl: './apk-edit.component.html',
  styleUrls: ['./apk-edit.component.css']
})
export class ApkEditComponent extends ApkDetailComponent implements OnInit {

  constructor(route: ActivatedRoute,
              http: JwtHttp,
              authService: AuthService) {
    super(route, http, authService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  get dataId() {
    return super.getId();
  }
}
