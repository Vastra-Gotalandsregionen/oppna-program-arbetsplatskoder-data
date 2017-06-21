import {Component, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {JwtHttp} from '../../../core/jwt-http';
import {Observable} from 'rxjs/Observable';
import {Prodn2} from '../../../model/prodn2';
import {Prodn1} from '../../../model/prodn1';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {RestResponse} from "../../../model/rest-response";
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {BasePaginatorComponent} from "../../../shared/base-paginator.component";

@Component({
  selector: 'app-prodn2-list',
  templateUrl: './prodn2-list.component.html',
  styleUrls: ['./prodn2-list.component.scss'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1, })),
      state('out', style({opacity: 0, height: '0', margin: 0, padding: 0, display: 'none'})),
      transition('* => *', animate('.2s ease'))
    ])
  ]
})
export class Prodn2ListComponent extends BasePaginatorComponent implements OnInit {

  response: RestResponse<Prodn2[]>;

  pageSubject = new BehaviorSubject<number>(0);

  prodn1s$: Observable<Prodn1[]>;

  selectedProdn1 = new BehaviorSubject<number>(null);

  filterProdn1: FormControl;

  showFilter = false;

  constructor(private http: JwtHttp,
              private location: Location,
              route: ActivatedRoute) {
    super();

    const page = route.snapshot.queryParams['page'];
    const prodn1 = route.snapshot.queryParams['prodn1'];

    this.filterProdn1 = new FormControl();

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
      .mergeMap(query => this.http.getPage('/api/prodn2' + query))
      .map(response => response.json())
      .share();

    response$.subscribe((restResponse: RestResponse<Prodn2[]>) => this.response = restResponse);

    this.prodn1s$.subscribe(c=>{
        this.filterProdn1.patchValue(this.selectedProdn1.value);
    });

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

  onFilterProdn1Change() {
    if (this.filterProdn1.value === '') {
      this.selectedProdn1.next(null);
    } else {
      this.selectedProdn1.next(this.filterProdn1.value);
    }
    this.pageSubject.next(0);
  }

}
