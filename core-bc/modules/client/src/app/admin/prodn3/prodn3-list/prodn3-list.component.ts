import {Component, OnInit} from '@angular/core';
import {Prodn3} from '../../../model/prodn3';
import {JwtHttp} from '../../../core/jwt-http';
import {RestResponse} from '../../../model/rest-response';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Prodn1} from '../../../model/prodn1';
import {Observable} from 'rxjs/Observable';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Prodn2} from "../../../model/prodn2";

@Component({
  selector: 'app-prodn3-list',
  templateUrl: './prodn3-list.component.html',
  styleUrls: ['./prodn3-list.component.scss'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1,})),
      state('out', style({opacity: 0, height: '0', 'margin-bottom': 0, padding: 0, display: 'none'})),
      transition('* => *', animate('.1s ease'))
    ])
  ]
})
export class Prodn3ListComponent implements OnInit {

  response: RestResponse<Prodn3[]>;

  pageSubject = new BehaviorSubject<number>(0);

  prodn1s$: Observable<Prodn1[]>;

  selectedProdn1 = new BehaviorSubject<string>(null);
  selectedProdn2 = new BehaviorSubject<string>(null);

  onlyOrphan = false;
  onlyOrphan$ = new BehaviorSubject<boolean>(false);

  onlyUnused = false;
  onlyUnused$ = new BehaviorSubject<boolean>(false);

  availableProdn2s$: Observable<Prodn2>;

  showFilter = false;

  constructor(private http: JwtHttp,
              private location: Location,
              route: ActivatedRoute) {

    const page = route.snapshot.queryParams['page'];
    const prodn1 = route.snapshot.queryParams['prodn1'];
    const prodn2 = route.snapshot.queryParams['prodn2'];
    const orphan = route.snapshot.queryParams['orphan'];
    const unused = route.snapshot.queryParams['unused'];

    if (page) {
      this.pageSubject.next(Number.parseInt(page));
    }

    if (prodn1) {
      this.selectedProdn1.next(prodn1);
    }

    if (prodn2) {
      this.selectedProdn2.next(prodn2);
    }

    if (prodn1 ||prodn2) {
      this.showFilter = true;
    }

    if (orphan) {
      this.onlyOrphan$.next(true);
      this.onlyOrphan = true;
    }

    if (unused) {
      this.onlyUnused$.next(true);
      this.onlyUnused = true;
    }
  }

  ngOnInit() {
    this.pageSubject
      .combineLatest(this.selectedProdn1, (page, selectedProdn1) => [page, selectedProdn1]) // Return array with page and selectedProdn1
      .combineLatest(this.selectedProdn2, (pageSelectedProdn1Array, selectedProdn2) => [...pageSelectedProdn1Array, selectedProdn2]) // Pass on array with page, selectedProdn1 and selectedProdn2
      .combineLatest(this.onlyOrphan$, (pageSelectedProdn1Array, onlyOrphan) => [...pageSelectedProdn1Array, onlyOrphan]) // Pass on array with page, selectedProdn1 and selectedProdn2
      .combineLatest(this.onlyUnused$, (pageSelectedProdn1Array, onlyUnused) => [...pageSelectedProdn1Array, onlyUnused]) // Pass on array with page, selectedProdn1 and selectedProdn2
      .map(pageSelectedProdn1Array => { // Create the URI
        const pageQuery = pageSelectedProdn1Array[0] > 0 ? '&page=' + pageSelectedProdn1Array[0] : null;
        const prodn1Query = pageSelectedProdn1Array[1] ? '&prodn1=' + pageSelectedProdn1Array[1] : null;
        const prodn2Query = pageSelectedProdn1Array[2] ? '&prodn2=' + pageSelectedProdn1Array[2] : null;
        const onlyOrphanQuery = pageSelectedProdn1Array[3] ? '&orphan=' + pageSelectedProdn1Array[3] : null;
        const onlyUnusedQuery = pageSelectedProdn1Array[4] ? '&unused=' + pageSelectedProdn1Array[4] : null;

        let query = [pageQuery, prodn1Query, prodn2Query, onlyOrphanQuery, onlyUnusedQuery].join('');

        if (query.length > 0) {
          query = '?' + query.substring(1);
        }

        return query;
      })
      .debounceTime(10)
      .do(query => {
        this.location.replaceState('/admin/prodn3' + query);
      })
      .mergeMap(query => this.http.get('/api/prodn3' + query))
      .map(response => response.json())
      .subscribe((restResponse: RestResponse<Prodn3[]>) => this.response = restResponse);

    this.prodn1s$ = this.http.get('/api/prodn1').map(response => response.json());

    this.availableProdn2s$ = this.selectedProdn1
      .filter(prodn1 => prodn1 !== null)
      .mergeMap(prodn1 => this.http.get('/api/prodn2?prodn1=' + prodn1))
      .map(response => response.json().content);

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
    return this.selectedProdn1.value === prodn1.producentid;
  }

  toggle(prodn1: Prodn1) {
    if (this.selectedProdn1.value === prodn1.producentid) {
      this.selectedProdn1.next(null);
      this.selectedProdn2.next(null);
    } else {
      this.selectedProdn1.next(prodn1.producentid);
      this.selectedProdn2.next(null);
    }
    this.pageSubject.next(0);
  }

  toggleProdn2(prodn2: Prodn2) {
    if (this.selectedProdn2.value === prodn2.producentid) {
      this.selectedProdn2.next(null);
    } else {
      this.selectedProdn2.next(prodn2.producentid);
    }
    this.pageSubject.next(0);
  }

  toggleOnlyOrphan() {
    if (this.onlyOrphan$.value) {
      this.onlyOrphan$.next(false);
    } else {
      this.onlyOrphan$.next(true);
      this.onlyUnused$.next(false);
      this.onlyUnused = false;
      this.selectedProdn1.next(null);
      this.selectedProdn2.next(null);
      this.pageSubject.next(0);
    }
  }

  toggleOnlyUnused() {
    if (this.onlyUnused$.value) {
      this.onlyUnused$.next(false);
    } else {
      this.onlyUnused$.next(true);
      this.onlyOrphan$.next(false);
      this.onlyOrphan = false;
      this.selectedProdn1.next(null);
      this.selectedProdn2.next(null);
      this.pageSubject.next(0);
    }
  }

  isSelectedProdn2(prodn2: Prodn2) {
    return this.selectedProdn2.value === prodn2.producentid;
  }
}
