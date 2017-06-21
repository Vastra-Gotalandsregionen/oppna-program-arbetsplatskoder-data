import {Component, OnInit, HostListener} from '@angular/core';
import {FormControl} from '@angular/forms';
import {Location} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Prodn3} from '../../../model/prodn3';
import {JwtHttp} from '../../../core/jwt-http';
import {RestResponse} from '../../../model/rest-response';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Prodn1} from '../../../model/prodn1';
import {Observable} from 'rxjs/Observable';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Prodn2} from "../../../model/prodn2";
import {BasePaginatorComponent} from "../../../shared/base-paginator.component";

@Component({
  selector: 'app-prodn3-list',
  templateUrl: './prodn3-list.component.html',
  styleUrls: ['./prodn3-list.component.scss'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1,})),
      state('out', style({opacity: 0, height: '0', margin: 0, padding: 0, display: 'none'})),
      transition('* => *', animate('.1s ease'))
    ])
  ]
})
export class Prodn3ListComponent extends BasePaginatorComponent implements OnInit {

  response: RestResponse<Prodn3[]>;

  pageSubject = new BehaviorSubject<number>(0);

  prodn1s$: Observable<Prodn1[]>;

  selectedProdn1 = new BehaviorSubject<number>(null);
  selectedProdn2 = new BehaviorSubject<number>(null);

  filterProdn1: FormControl;
  filterProdn2: FormControl;

  onlyUnused = false;
  onlyUnused$ = new BehaviorSubject<boolean>(false);

  availableProdn2s$: Observable<Prodn2>;

  showFilter = false;

  constructor(private http: JwtHttp,
              private location: Location,
              route: ActivatedRoute) {

    super();

    const page = route.snapshot.queryParams['page'];
    const prodn1 = route.snapshot.queryParams['prodn1'];
    const prodn2 = route.snapshot.queryParams['prodn2'];
    const unused = route.snapshot.queryParams['unused'];

    this.filterProdn1 = new FormControl();
    this.filterProdn2 = new FormControl();

    if (page) {
      this.pageSubject.next(Number.parseInt(page));
    }

    if (prodn1) {
      this.selectedProdn1.next(Number.parseInt(prodn1));
    }

    if (prodn2) {
      this.selectedProdn2.next(Number.parseInt(prodn2));
    }

    if (prodn1 || prodn2) {
      this.showFilter = true;
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
      .combineLatest(this.onlyUnused$, (pageSelectedProdn1Array, onlyUnused) => [...pageSelectedProdn1Array, onlyUnused]) // Pass on array with page, selectedProdn1 and selectedProdn2
      .map(pageSelectedProdn1Array => { // Create the URI
        const pageQuery = pageSelectedProdn1Array[0] > 0 ? '&page=' + pageSelectedProdn1Array[0] : null;
        const prodn1Query = pageSelectedProdn1Array[1] ? '&prodn1=' + pageSelectedProdn1Array[1] : null;
        const prodn2Query = pageSelectedProdn1Array[2] ? '&prodn2=' + pageSelectedProdn1Array[2] : null;
        const onlyUnusedQuery = pageSelectedProdn1Array[3] ? '&unused=' + pageSelectedProdn1Array[3] : null;

        let query = [pageQuery, prodn1Query, prodn2Query, /*onlyOrphanQuery, */onlyUnusedQuery].join('');

        if (query.length > 0) {
          query = '?' + query.substring(1);
        }

        return query;
      })
      .debounceTime(10)
      .do(query => {
        this.location.replaceState('/admin/prodn3' + query);
      })
      .mergeMap(query => this.http.getPage('/api/prodn3' + query))
      .map(response => response.json())
      .subscribe((restResponse: RestResponse<Prodn3[]>) => this.response = restResponse);

    this.prodn1s$ = this.http.get('/api/prodn1').map(response => response.json());

    this.availableProdn2s$ = this.selectedProdn1
      .filter(prodn1 => prodn1 !== null)
      .mergeMap(prodn1 => this.http.get('/api/prodn2?prodn1=' + prodn1))
      .map(response => response.json().content);

      this.prodn1s$.subscribe(c=>{
          this.filterProdn1.patchValue(this.selectedProdn1.value);
      });

      this.availableProdn2s$.subscribe(c=>{
          this.filterProdn2.patchValue(this.selectedProdn2.value);
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

  isSelected(prodn1: Prodn1) {
    return this.selectedProdn1.value === prodn1.id;
  }

  toggle(prodn1: Prodn1) {
    if (this.selectedProdn1.value === prodn1.id) {
      this.selectedProdn1.next(null);
      this.selectedProdn2.next(null);
    } else {
      this.selectedProdn1.next(prodn1.id);
      this.selectedProdn2.next(null);
    }
    this.pageSubject.next(0);
  }

  toggleProdn2(prodn2: Prodn2) {
    if (this.selectedProdn2.value === prodn2.id) {
      this.selectedProdn2.next(null);
    } else {
      this.selectedProdn2.next(prodn2.id);
    }
    this.pageSubject.next(0);
  }

  toggleOnlyUnused() {
    if (this.onlyUnused$.value) {
      this.onlyUnused$.next(false);
    } else {
      this.onlyUnused$.next(true);
      this.pageSubject.next(0);
    }
  }

  isSelectedProdn2(prodn2: Prodn2) {
    return this.selectedProdn2.value === prodn2.id;
  }

  onFilterProdn1Change() {
    if (this.filterProdn1.value === '') {
      this.selectedProdn1.next(null);
      this.selectedProdn2.next(null);
    } else {
      this.selectedProdn1.next(this.filterProdn1.value);
      this.selectedProdn2.next(null);
    }
    this.pageSubject.next(0);
  }

  onFilterProdn2Change() {
    if (this.filterProdn2.value === '') {
      this.selectedProdn2.next(null);
    } else {
      this.selectedProdn2.next(this.filterProdn2.value);
    }
    this.pageSubject.next(0);
  }

}
