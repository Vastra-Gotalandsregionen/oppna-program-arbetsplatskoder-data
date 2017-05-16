import {Component} from '@angular/core';
import {Location} from "@angular/common";

import {ApkBase} from "../apk-base/apk-base";

@Component({
  selector: 'app-apk-create',
  templateUrl: './apk-create.component.html',
  styleUrls: ['./apk-create.component.css'],
})
export class ApkCreateComponent extends ApkBase {

  location: Location;

  constructor(location: Location) {
    super(location);
    this.location = location;
  }


}
