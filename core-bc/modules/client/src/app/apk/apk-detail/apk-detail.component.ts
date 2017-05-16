import {Component, OnInit} from '@angular/core';
import {Location} from "@angular/common";
import {ActivatedRoute} from '@angular/router';
import {Http} from '@angular/http';
import {Data} from '../../model/data';
import {AuthService} from '../../core/auth/auth.service';
import {StateService} from '../../core/state/state.service';
import {ApkBase} from "../apk-base/apk-base";

@Component({
  selector: 'app-apk-detail',
  templateUrl: './apk-detail.component.html',
  styleUrls: ['./apk-detail.component.css']
})
export class ApkDetailComponent extends ApkBase implements OnInit {

  id: string;
  data: Data;
  location: Location;
  stateService : StateService;

  constructor(protected route: ActivatedRoute,
              protected http: Http,
              location: Location,
              stateService: StateService,
              protected authService: AuthService) {
    super(location, stateService);
    this.location = location;
    this.stateService = stateService;
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params);
      this.id = params.id;

      if (this.id) {
        this.http.get('/api/data/' + this.id)
          .map(response => response.json())
          .subscribe((data: Data) => {
            this.data = data;
          });
      }
    });
  }

  protected getId() {
    return this.id;
  }

  userHasEditPermission(data: Data) {
    return this.authService.userHasDataEditPermission(data);
  }
}
