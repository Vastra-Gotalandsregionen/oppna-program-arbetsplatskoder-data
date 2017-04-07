import {FormControl} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import {Http, RequestOptions, URLSearchParams, Headers} from '@angular/http';
import {Data} from '../model/data';
import {RestResponse} from '../model/rest-response';
import {Observable} from 'rxjs';
import {Location} from '@angular/common';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-apk',
  templateUrl: './apk.component.html',
  styleUrls: ['./apk.component.css']
})
export class ApkComponent implements OnInit {

  // paginatorData: PaginatorData;
  stateCtrl: FormControl;
  query: string;
  page: number = 0;
// labelWithLinks: LabelWithLink[] = [];
  datas: Data[];
  response: RestResponse<Data>;

  constructor(private http: Http,
              private location: Location,
              private route: ActivatedRoute) {
    this.stateCtrl = new FormControl();
  }

  ngOnInit() {
    this.route.queryParams
      .subscribe(params => {
        console.log(params.query);

        this.query = params.query;

        if (params.page) {
          this.page = Number(params.page);
        }

        this.fetchDatas();

        this.stateCtrl.valueChanges
          .startWith(null)
          .subscribe(query => {
            if (query) {
              this.location.replaceState('/apk', 'query=' + query + '&page=' + this.page);
            } else if (this.page > 0) {
              this.location.replaceState('/apk', 'page=' + this.page);
            } else {
              this.location.replaceState('/apk');
            }
            this.fetchDatas();
          });
      });


    /*.switchMap(query => {
     this.query = query;
     return this.observeData();
     })
     .subscribe(response => {
     this.handleResponse(response);
     });*/
  }

  private fetchDatas() {
    this.observeData()
      .subscribe(response => {
        this.handleResponse(response);
      });
  }

  private handleResponse(response) {
    this.response = response;

    if (this.page + 1 > this.response.totalPages) {
      this.page = 0;

      if (this.response.totalPages > 0) {
        this.fetchDatas();
      }
    }

    if (this.query) {
      this.location.replaceState('/apk', 'query=' + this.query + '&page=' + this.page);
    } else if (this.page > 0) {
      this.location.replaceState('/apk', 'page=' + this.page);
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

    const requestOptions = new RequestOptions();
    requestOptions.params = params;
    requestOptions.headers = new Headers({'Content-Type': 'application/json; charset=UTF-8'});

    return this.http.get('/api/data', requestOptions)

    // return this.http.get('/api/data', {params: {page: this.page}})
      .map(response => response.json());
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

}

/*class PaginatorData {
 size: number;
 totalElements: number;
 totalPages: number;
 number: number;
 }*/

/*
 class LabelWithLink {
 label: string;
 link: string;

 constructor(label: string, link: string) {
 this.label = label;
 this.link = link;
 }
 }
 */
