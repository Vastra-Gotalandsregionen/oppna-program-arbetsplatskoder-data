import {Component} from '@angular/core';

import {ApkDetailComponent} from "../apk-detail/apk-detail.component";
import {AuthService} from "../../core/auth/auth.service";
import {Http} from "@angular/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-apk-create',
  templateUrl: './apk-create.component.html',
  styleUrls: ['./apk-create.component.css'],
})
export class ApkCreateComponent extends ApkDetailComponent {

  location: Location;

  constructor(route: ActivatedRoute,
              http: Http,
              authService: AuthService) {
    super(route, http, authService);
  }

}
