import {Component} from '@angular/core';

import {ApkDetailComponent} from "../apk-detail/apk-detail.component";
import {AuthService} from "../../core/auth/auth.service";
import {ActivatedRoute} from "@angular/router";
import {JwtHttp} from "../../core/jwt-http";

@Component({
  selector: 'app-apk-create',
  templateUrl: './apk-create.component.html',
  styleUrls: ['./apk-create.component.css'],
})
export class ApkCreateComponent extends ApkDetailComponent {

  location: Location;

  constructor(route: ActivatedRoute,
              http: JwtHttp,
              authService: AuthService) {
    super(route, http, authService);
  }

}
