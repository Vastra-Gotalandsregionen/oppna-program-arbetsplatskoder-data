import {Component} from '@angular/core';

import {StateService} from '../../core/state/state.service';

import {ApkBase} from "../apk-base/apk-base";

@Component({
  selector: 'app-apk-create',
  templateUrl: './apk-create.component.html',
  styleUrls: ['./apk-create.component.css'],
})
export class ApkCreateComponent extends ApkBase {

  location: Location;
  stateService : StateService;

  constructor(stateService : StateService) {
    super(stateService);
    this.stateService = stateService;
  }


}
