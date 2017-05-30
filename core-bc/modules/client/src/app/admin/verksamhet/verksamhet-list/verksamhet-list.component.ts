import { Component, OnInit } from '@angular/core';
import {JwtHttp} from "../../../core/jwt-http";
import {Verksamhet} from "../../../model/verksamhet";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-verksamhet-list',
  templateUrl: './verksamhet-list.component.html',
  styleUrls: ['./verksamhet-list.component.scss']
})
export class VerksamhetListComponent implements OnInit {

  verksamhets$: Observable<Verksamhet[]>;

  constructor(private http: JwtHttp) { }

  ngOnInit() {

    this.verksamhets$ = this.http.getPage('/api/verksamhet')
      .map(response => response.json());
  }

}
