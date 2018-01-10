import {FormControl} from '@angular/forms';
import {Location} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {RequestOptions, URLSearchParams, Headers, Response} from '@angular/http';
import {Data} from '../../model/data';
import {RestResponse} from '../../model/rest-response';
import {Observable} from 'rxjs/Observable';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../core/auth/auth.service';
import {JwtHttp} from '../../core/jwt-http';
import {Prodn1} from '../../model/prodn1';
import {MatDialog, MatSnackBar} from "@angular/material";
import {ConfirmDialogComponent} from "../../shared/confirm-dialog/confirm-dialog.component";
import {ApkBase} from "../apk-base/apk-base";
import {HostListener} from "@angular/core";

@Component({
  selector: 'app-apk',
  templateUrl: './apk.component.html',
  styleUrls: ['./apk.component.css']
})
export class ApkComponent extends ApkBase implements OnInit {

  stateCtrl: FormControl;
  onlyActiveDatasCtrl: FormControl;
  onlyMyDatasCtrl: FormControl;

  query: string;
  page = 0;
  selectedPage = 1;
  totalPagesArr: Array<number>;
  onlyActiveDatas: boolean;
  onlyMyDatas: boolean;
  location: Location;

  response: RestResponse<Data>;
  sort: { field: string, ascending: boolean };
  usersProdn1sString$: Observable<string>;

  constructor(private http: JwtHttp,
              location: Location,
              private route: ActivatedRoute,
              private authService: AuthService,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) {
    super();
    this.location = location;
    this.stateCtrl = new FormControl();
    this.onlyActiveDatasCtrl = new FormControl();
    this.onlyMyDatasCtrl = new FormControl();
  }

  ngOnInit() {
    this.route.queryParams
      .subscribe(params => {

        this.query = params.query;

        if (params.page) {
          this.page = Number(params.page);
          this.selectedPage = this.page + 1;
        }

        if (params.sort) {
          this.sort = {field: params.sort, ascending: params.asc === 'true'}
        }

        if (params.onlyActiveDatas) {
          this.onlyActiveDatas = params.onlyActiveDatas === 'true';
        } else {
          //this.onlyActiveDatas = this.loggedIn;
        }

        if (params.onlyMyDatas) {
          this.onlyMyDatas = params.onlyMyDatas === 'true';
        } else {
          //this.onlyMyDatas = this.loggedIn;
        }


        this.fetchDatas();

        this.stateCtrl.valueChanges
          .skip(1) // Skip on init
          .debounceTime(50) // Primarily to avoid many requests if user presses and holds backspace button.
          .subscribe(query => {
            this.query = query;
            this.updateState();
          });

          this.onlyActiveDatasCtrl.valueChanges
          .skip(1) // Skip on init
          .subscribe(value => {
            this.onlyActiveDatas = value;
            this.updateState();
          });

        this.onlyMyDatasCtrl.valueChanges
          .skip(1) // Skip on init
          .subscribe(value => {
            this.onlyMyDatas = value;
            this.updateState();
          });

      });

    if (this.authService.jwt) {
      this.usersProdn1sString$ = this.http.get('/api/prodn1')
        .map(response => response.json())
        .map((prodn1: Prodn1[]) => prodn1.map(prodn1 => prodn1.kortnamn).join(', '));
    } else {
      this.usersProdn1sString$ = Observable.from('');
    }
  }

  private updateState() {
    if (this.query || this.page > 0 || this.sort || this.onlyMyDatas || this.onlyActiveDatas) {
      const queryPart = (this.query ? '&query=' + this.query : '');
      const pagePart = (this.page > 0 ? '&page=' + this.page : '');
      const sortPart = (this.sort ? '&sort=' + this.sort.field + '&asc=' + this.sort.ascending : '');
      const onlyMyDatasPart = (this.onlyMyDatas ? `&onlyMyDatas=${this.onlyMyDatas}` : '');
      const onlyActiveDatasPart = (this.onlyActiveDatas ? `&onlyActiveDatas=${this.onlyActiveDatas}` : '');

      let fullQueryPart = queryPart + pagePart + sortPart + onlyMyDatasPart + onlyActiveDatasPart;

      if (fullQueryPart.startsWith('&')) {
        fullQueryPart = fullQueryPart.substring(1);
      }

      this.location.replaceState('/apk', fullQueryPart);
    } else {
      this.location.replaceState('/apk');
    }

    this.selectedPage = this.page + 1;

    this.fetchDatas();
  }

  private fetchDatas() {
    this.observeData()
      .subscribe(response => {
        this.handleResponse(response);
      });
  }

  private handleResponse(response) {
    this.response = response;

    this.totalPagesArr = new Array(0);
    for(let i = 1; i <= this.response.totalPages; i++) {
      this.totalPagesArr.push(i);
    }

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

    if (this.onlyActiveDatas) {
      params.set('onlyActiveDatas', this.onlyActiveDatas + '');
    }

    if (this.onlyMyDatas) {
      params.set('onlyMyDatas', this.onlyMyDatas + '');
    }

    const requestOptions = new RequestOptions();
    requestOptions.params = params;

    return this.http.get('/api/data', requestOptions).map(response => response.json());
  }

  @HostListener('window:keydown', ['$event'])
  paginateByArrowKey($event) {
    if ($event.key) {
      if ($event.key.toLowerCase().indexOf('right') > -1) {
        this.nextPage();
      } else if ($event.key.toLowerCase().indexOf('left') > -1) {
        this.previousPage()
      }
    }
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

  goToSelectedPage(): void {
    this.page = this.selectedPage - 1;
    this.updateState();
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

  get admin() {
    return this.authService.isAdmin();
  }

  // getStatus(data: Data) {
  //
  //   const tillDatum = data.tillDatum;
  //
  //   if (!tillDatum) {
  //     // No value means "until further notice".
  //     return 'valid';
  //   }
  //
  //   if (Util.isOlderThanXYears(tillDatum, 1)) {
  //     return 'fullyClosed';
  //   } else if (Util.isOlderThanXYears(tillDatum, 0)) {
  //     return 'closed'
  //   } else {
  //     return 'valid';
  //   }
  //
  // }

  confirmDelete(data: Data) {
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        text: 'Är du säker att du vill ta bort vald arbetsplatskod?',
        confirmButtonText: 'Ta bort'
      },
      panelClass: 'apk-dialog'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') {
        this.http.delete('/api/data/' + data.id)
          .subscribe(response => {
            console.log(response);
            this.updateState();
            this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          });
      }
    });
  }

}
