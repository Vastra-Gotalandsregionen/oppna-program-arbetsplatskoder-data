import { Component, OnInit } from '@angular/core';
import {JwtHttp} from "../../../core/jwt-http";
import {Vardform} from "../../../model/vardform";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-vardform-list',
  templateUrl: './vardform-list.component.html',
  styleUrls: ['./vardform-list.component.scss']
})
export class VardformListComponent implements OnInit {

  vardforms$: Observable<Vardform[]>;

  constructor(private http: JwtHttp) { }

  ngOnInit() {

    this.vardforms$ = this.http.getPage('/api/vardform')
      .map(response => response.json().content);
  }

}
