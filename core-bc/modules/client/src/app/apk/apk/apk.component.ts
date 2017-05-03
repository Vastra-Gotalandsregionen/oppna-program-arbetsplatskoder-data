import {FormControl} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import {RequestOptions, URLSearchParams, Headers} from '@angular/http';
import {Data} from '../../model/data';
import {RestResponse} from '../../model/rest-response';
import {Observable} from 'rxjs/Observable';
import {Location} from '@angular/common';
import {ActivatedRoute} from "@angular/router";
import {ErrorHandler} from "../../shared/error-handler";
import {AuthService} from "../../core/auth/auth.service";
import {JwtHttp} from "../../core/jwt-http";
import {Prodn1} from "../../model/prodn1";

@Component({
  selector: 'app-apk',
  templateUrl: './apk.component.html',
  styleUrls: ['./apk.component.css']
})
export class ApkComponent implements OnInit {

  stateCtrl: FormControl;
  onlyMyDatasCtrl: FormControl;

  query: string;
  page: number = 0;
  onlyMyDatas: boolean;

  datas: Data[];
  response: RestResponse<Data>;
  sort: { field: string, ascending: boolean };
  usersProdn1sString$: Observable<string>;

  constructor(private http: JwtHttp,
              private location: Location,
              private route: ActivatedRoute,
              private authService: AuthService,
              private errorHandler: ErrorHandler) {
    this.stateCtrl = new FormControl();
    this.onlyMyDatasCtrl = new FormControl();
  }

  ngOnInit() {
    this.route.queryParams
      .subscribe(params => {

        this.query = params.query;

        if (params.page) {
          this.page = Number(params.page);
        }

        if (params.sort) {
          this.sort = {field: params.sort, ascending: params.asc === 'true'}
        }

        if (params.onlyMyDatas) {
          this.onlyMyDatas = params.onlyMyDatas === 'true';
        }

        this.fetchDatas();

        this.stateCtrl.valueChanges
          .subscribe(query => {
            this.query = query;
            this.updateState();
          });

        this.onlyMyDatasCtrl.valueChanges.subscribe(value => {
          this.onlyMyDatas = value;
          this.updateState();
        });

      });

    if (this.authService.jwt) {
      this.usersProdn1sString$ = this.http.get('/api/prodn1')
        .map(response => response.json())
        .map((prodn1: Prodn1[]) => prodn1.map(prodn1 => prodn1.foretagsnamn).join(', '));
    } else {
      this.usersProdn1sString$ = Observable.from('');
    }
  }

  private updateState() {
    if (this.query || this.page > 0 || this.sort || this.onlyMyDatas) {
      let queryPart = (this.query ? '&query=' + this.query : '');
      let pagePart = (this.page > 0 ? '&page=' + this.page : '');
      let sortPart = (this.sort ? '&sort=' + this.sort.field + '&asc=' + this.sort.ascending : '');
      let onlyMyDatasPart = (this.onlyMyDatas ? `&onlyMyDatas=${this.onlyMyDatas}` : '');

      let fullQueryPart = queryPart + pagePart + sortPart + onlyMyDatasPart;

      if (fullQueryPart.startsWith('&')) {
        fullQueryPart = fullQueryPart.substring(1);
      }

      this.location.replaceState('/apk', fullQueryPart);
    } else {
      this.location.replaceState('/apk');
    }

    this.fetchDatas();
  }

  private fetchDatas() {
    this.observeData()
      .subscribe(response => {
        this.handleResponse(response);
      }, error => this.errorHandler.notifyError(error));
  }

  private handleResponse(response) {
    this.response = response;

    if (this.page + 1 > this.response.totalPages) {
      // Reset pages and refetch.
      this.page = 0;

      if (this.response.totalPages > 0) {
        this.updateState();
      }
    }
  }

  private observeData(): Observable<any> {
    const params: URLSearchParams = new URLSearchParams();

    params.set('page', this.page + '');

    if (this.query) {
      params.set('query', this.query);
    }

    if (this.sort) {
      params.set('sort', this.sort.field);
      params.set('asc', this.sort.ascending + '');
    }

    if (this.onlyMyDatas) {
      params.set('onlyMyDatas', this.onlyMyDatas + '');
    }

    const requestOptions = new RequestOptions();
    requestOptions.params = params;
    requestOptions.headers = new Headers({'Content-Type': 'application/json; charset=UTF-8'});

    return this.http.get('/api/data', requestOptions).map(response => response.json());
  }

  nextPage(): void {
    if (this.page + 1 < this.response.totalPages) {
      this.page++;
      this.updateState();
    }
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.updateState();
    }
  }

  toggleSort(field: string) {
    if (this.sort && this.sort.field === field) {
      const currentAscending = this.sort.ascending;
      this.sort = {field: field, ascending: !currentAscending};
    } else {
      this.sort = {field: field, ascending: true}
    }

    this.updateState();
  }

  userHasEditPermission(data: Data) {
    return this.authService.userHasDataEditPermission(data);
  }

  get loggedIn() {
    return this.authService.jwt ? true : false;
  }

}
