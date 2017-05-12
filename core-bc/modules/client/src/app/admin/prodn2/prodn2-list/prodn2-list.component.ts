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

  selectedProdn1 = new BehaviorSubject<string>(null);

  orphanProdn2s: string[] = [];

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
      this.selectedProdn1.next(prodn1);
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

    // Find Prodn2s where their N1 doesn't exist.
    response$
      .delay(200) // Prioritize the listing so wait before we start this processing.
      .map(restResponse => restResponse.content)
      .mergeMap((prodn2s: Prodn2[]) => prodn2s) // To single items
      .map((prodn2: Prodn2) => prodn2.n1) // Make this mapping in order to be able to make distinct after this step.
      .distinct() // Many Prodn2s share the same N1 so we don't need to make a request for all.
      .mergeMap((prodn2N1: string) => { // To forward both the Prodn2, which we will store in our field, and the Prodn1, which we will use to filter.
        const prodn1$ = this.http.get('/api/prodn1?producentid=' + prodn2N1);
        const prodn2N1$ = Observable.of(prodn2N1);
        return Observable.forkJoin(prodn1$, prodn2N1$);
      })
      .filter(response => { // Only forward those where we didn't find any Prodn1
        const noHit = response[0].json()[0] === null;
        return noHit;
      })
      .map(response => response[1]) // Pass prodn2N1 string
      .subscribe(prodn21 => this.orphanProdn2s.push(prodn21));
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
    } else {
      this.selectedProdn1.next(prodn1.producentid);
    }
    this.pageSubject.next(0);
  }
}
