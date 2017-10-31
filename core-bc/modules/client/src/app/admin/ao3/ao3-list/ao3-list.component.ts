import { Component, OnInit } from '@angular/core';
import {JwtHttp} from "../../../core/jwt-http";
import {RestResponse} from "../../../model/rest-response";
import {Ao3} from "../../../model/ao3";
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {BasePaginatorComponent} from "../../../shared/base-paginator.component";
import {Prodn1} from "../../../model/prodn1";

@Component({
  selector: 'app-ao3-list',
  templateUrl: './ao3-list.component.html',
  styleUrls: ['./ao3-list.component.scss']
})
export class Ao3ListComponent extends BasePaginatorComponent implements OnInit {

  response: RestResponse<Ao3[]>;

  pageSubject = new BehaviorSubject<number>(0);

  constructor(private http: JwtHttp,
              private location: Location,
              route: ActivatedRoute) {

    super();

    const page = route.snapshot.queryParams['page'];

    if (page) {
      this.pageSubject.next(Number.parseInt(page));
    }

  }

  ngOnInit() {
    this.pageSubject
      .map(page => { // Create the URI
        return page && page > 0 ? page : null;
      })
      .do(page => {
        this.location.replaceState('/admin/ao3', page ? '?page=' + page : '');
      })
      .mergeMap(page => this.http.getPage('/api/ao3', page))
      .map(response => response.json())
      .subscribe((restResponse: RestResponse<Ao3[]>) => this.response = restResponse);

  }

  nextPage() {
    if (this.pageSubject.value < this.response.totalPages - 1) {
      this.pageSubject.next(this.pageSubject.value + 1);
    }
  }

  previousPage() {
    if (this.pageSubject.value > 0) {
      this.pageSubject.next(this.pageSubject.value - 1);
    }
  }

}
