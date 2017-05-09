import {Component, OnInit} from '@angular/core';
import {Prodn3} from "../../model/prodn3";
import {JwtHttp} from "../../core/jwt-http";
import {RestResponse} from "../../model/rest-response";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {Location} from '@angular/common';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-prodn3-list',
  templateUrl: './prodn3-list.component.html',
  styleUrls: ['./prodn3-list.component.scss']
})
export class Prodn3ListComponent implements OnInit {

  response: RestResponse<Prodn3[]>;

  pageSubject = new BehaviorSubject<number>(0);

  constructor(private http: JwtHttp,
              private location: Location,
              route: ActivatedRoute) {

    let page = route.snapshot.queryParams['page'];
    if (page) {
      this.pageSubject.next(Number.parseInt(page));
    }
  }

  ngOnInit() {
    this.pageSubject
      .do(page => this.location.replaceState('/prodn3' + (page > 0 ? '?page=' + page : '')))
      .mergeMap(page => this.http.get('/api/prodn3?page=' + page))
      .map(response => response.json())
      .subscribe((restResponse: RestResponse<Prodn3[]>) => this.response = restResponse);
  }

  nextPage() {
    if (this.pageSubject.value < this.response.totalPages -1) {
      this.pageSubject.next(this.pageSubject.value + 1);
    }
  }

  previousPage() {
    if (this.pageSubject.value > 0) {
      this.pageSubject.next(this.pageSubject.value -1);
    }
  }
}
