import { Component, OnInit } from '@angular/core';
import {ApkDetailComponent} from "../apk-detail/apk-detail.component";
import {Http} from "@angular/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-apk-edit',
  templateUrl: './apk-edit.component.html',
  styleUrls: ['./apk-edit.component.css']
})
export class ApkEditComponent extends ApkDetailComponent implements OnInit {

  constructor(protected route: ActivatedRoute,
              protected http: Http) {
    super(route, http);
  }

  ngOnInit() {
    super.ngOnInit();
  }

}
