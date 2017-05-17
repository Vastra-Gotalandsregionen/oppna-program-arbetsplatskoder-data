import {Component, OnInit} from '@angular/core';
import {JwtHttp} from '../../../core/jwt-http';
import {Observable} from 'rxjs/Observable';
import {Prodn2} from '../../../model/prodn2';
import {Prodn1} from '../../../model/prodn1';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {RestResponse} from "../../../model/rest-response";
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-prodn2-list',
  templateUrl: './prodn2-list.component.html',
  styleUrls: ['./prodn2-list.component.scss'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1, })),
      state('out', style({opacity: 0, height: '0', 'margin-bottom': 0, padding: 0, display: 'none'})),
      transition('* => *', animate('.2s ease'))
    ])
  ]
})
export class Prodn2ListComponent implements OnInit {

  response: RestResponse<Prodn2[]>;

  pageSubject = new BehaviorSubject<number>(0);

  prodn1s$: Observable<Prodn1[]>;

  selectedProdn1 = new BehaviorSubject<number>(null);

  showFilter = false;

  constructor(private http: JwtHttp,
              private location: Location,
              route: ActivatedRoute) {

    const page = route.snapshot.queryParams['page'];
    const prodn1 = route.snapshot.queryParams['prodn1'];

    if (page) {
      this.pageSubject.next(Number.parseInt(page));
    }

    if (prodn1) {
      this.selectedProdn1.next(Number.parseInt(prodn1));
      this.showFilter = true;
    }
  }

  ngOnInit() {

    this.prodn1s$ = this.http.get('/api/prodn1').map(response => response.json());

    const response$ = this.pageSubject
      .combineLatest(this.selectedProdn1, (page, selectedProdn1) => [page, selectedProdn1]) // Return array with page and selectedProdn1
      .map(pageSelectedProdn1Array => { // Create the URI
        const pageQuery = pageSelectedProdn1Array[0] > 0 ? '&page=' + pageSelectedProdn1Array[0] : null;
        const prodn1Query = pageSelectedProdn1Array[1] ? '&prodn1=' + pageSelectedProdn1Array[1] : null;

        let query = [pageQuery, prodn1Query].join('');

        if (query.length > 0) {
          query = '?' + query.substring(1);
        }

        return query;
      })
      .debounceTime(10) // To avoid multiple requests when both selected prodn1 and page changes concurrently
      .do(query => {
        this.location.replaceState('/admin/prodn2' + query);
      })
      .mergeMap(query => this.http.get('/api/prodn2' + query))
      .map(response => response.json())
      .share();

    response$.subscribe((restResponse: RestResponse<Prodn2[]>) => this.response = restResponse);
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

  isSelected(prodn1: Prodn1) {
    return this.selectedProdn1.value === prodn1.id;
  }

  toggle(prodn1: Prodn1) {
    if (this.selectedProdn1.value === prodn1.id) {
      this.selectedProdn1.next(null);
    } else {
      this.selectedProdn1.next(prodn1.id);
    }
    this.pageSubject.next(0);
  }
}
