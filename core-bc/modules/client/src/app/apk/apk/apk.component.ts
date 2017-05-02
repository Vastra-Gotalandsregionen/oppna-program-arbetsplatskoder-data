import {FormControl} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import {Http, RequestOptions, URLSearchParams, Headers} from '@angular/http';
import {Data} from '../../model/data';
import {RestResponse} from '../../model/rest-response';
import {Observable} from 'rxjs/Observable';
import {Location} from '@angular/common';
import {ActivatedRoute} from "@angular/router";
import {isUndefined} from "util";
import {ErrorHandler} from "../../shared/error-handler";
import {AuthService} from "../../core/auth/auth.service";
import {JwtHttp} from "../../core/jwt-http";

@Component({
  selector: 'app-apk',
  templateUrl: './apk.component.html',
  styleUrls: ['./apk.component.css']
})
export class ApkComponent implements OnInit {

  stateCtrl: FormControl;
  query: string;
  page: number = 0;
  datas: Data[];
  response: RestResponse<Data>;
  sort: { field: string, ascending: boolean };

  constructor(private http: JwtHttp,
              private location: Location,
              private route: ActivatedRoute,
              private authService: AuthService,
              private errorHandler: ErrorHandler) {
    this.stateCtrl = new FormControl();
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

        this.fetchDatas();

        this.stateCtrl.valueChanges
          .subscribe(query => {

            if (query || this.page > 0 || this.sort) {
              this.location.replaceState('/apk', (query ? '&query=' + query : '') + (this.page > 0 ? '&page=' + this.page : '') + (this.sort ? '&sort=' + this.sort.field + '&asc=' + this.sort.ascending: ''));
            } else {
              this.location.replaceState('/apk');
            }

            if (!isUndefined(query)) {
              this.fetchDatas();
            }
          });
      });
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
      this.page = 0;

      if (this.response.totalPages > 0) {
        this.fetchDatas();
      }
    }

    if (this.query || this.page > 0 || this.sort) {
      this.location.replaceState('/apk', (this.query ? '&query=' + this.query : '') + (this.page > 0 ? '&page=' + this.page : '') + (this.sort ? '&sort=' + this.sort.field + '&asc=' + this.sort.ascending: ''));
    } else {
      this.location.replaceState('/apk');
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

    const requestOptions = new RequestOptions();
    requestOptions.params = params;
    requestOptions.headers = new Headers({'Content-Type': 'application/json; charset=UTF-8'});

    return this.http.get('/api/data', requestOptions).map(response => response.json());
  }

  nextPage(): void {
    if (this.page + 1 < this.response.totalPages) {
      this.page++;
      this.fetchDatas();
    }
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.fetchDatas();
    }
  }

  toggleSort(field: string) {
    if (this.sort && this.sort.field === field) {
      const currentAscending = this.sort.ascending;
      this.sort = {field: field, ascending: !currentAscending};
    } else {
      this.sort = {field: field, ascending: true}
    }

    this.fetchDatas();
  }

  userHasEditPermission(data: Data) {
    return this.authService.userHasDataEditPermission(data);
  }

}
