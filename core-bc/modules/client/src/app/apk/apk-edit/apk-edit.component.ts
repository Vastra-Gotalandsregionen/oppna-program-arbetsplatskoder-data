import { Component, OnInit } from '@angular/core';
import {ApkDetailComponent} from '../apk-detail/apk-detail.component';
import {Http} from '@angular/http';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../core/auth/auth.service';
import {StateService} from '../../core/state/state.service';

@Component({
  selector: 'app-apk-edit',
  templateUrl: './apk-edit.component.html',
  styleUrls: ['./apk-edit.component.css']
})
export class ApkEditComponent extends ApkDetailComponent implements OnInit {

  stateService: StateService;

  constructor(route: ActivatedRoute,
              http: Http,
              stateService: StateService,
              authService: AuthService) {
    super(route, http, stateService, authService);
    this.stateService = stateService;
  }

  ngOnInit() {
    super.ngOnInit();
  }

  get dataId() {
    return super.getId();
  }
}
