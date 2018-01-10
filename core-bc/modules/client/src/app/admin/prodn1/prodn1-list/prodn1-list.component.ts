import {Component, OnInit} from '@angular/core';
import {JwtHttp} from '../../../core/jwt-http';
import {Observable} from 'rxjs/Observable';
import {Prodn1} from '../../../model/prodn1';
import {Response} from '@angular/http';
import {AuthService} from "../../../core/auth/auth.service";

@Component({
  selector: 'app-prodn1-list',
  templateUrl: './prodn1-list.component.html',
  styleUrls: ['./prodn1-list.component.scss']
})
export class Prodn1ListComponent implements OnInit {

  prodn1s$: Observable<Prodn1[]>;
  orphanProdn1s: number[] = [];

  constructor(private http: JwtHttp,
              private authService: AuthService) {
  }

  ngOnInit() {

    this.prodn1s$ = this.http.get('/api/prodn1').map(response => response.json()).share();

    this.http.get('/api/prodn1?orphan=true')
      .map<Response, Prodn1[]>(response => <Prodn1[]>response.json()) // To array
      .mergeMap((prodn1: Prodn1[]) => prodn1) // To single items
      .map((prodn1: Prodn1) => prodn1.id).toArray()
        .subscribe(value => this.orphanProdn1s = value);
  }

  get admin() {
    return this.authService.isAdmin();
  }

}
