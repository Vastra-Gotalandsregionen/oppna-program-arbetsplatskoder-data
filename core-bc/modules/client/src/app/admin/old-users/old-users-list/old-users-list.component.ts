import { Component, OnInit } from '@angular/core';
import {Response} from '@angular/http';
import {Anvandare} from '../../../model/anvandare';
import {ErrorHandler} from '../../../shared/error-handler';
import {StateService} from '../../../core/state/state.service';
import {JwtHttp} from '../../../core/jwt-http';

@Component({
  selector: 'app-old-users-list',
  templateUrl: './old-users-list.component.html',
  styleUrls: ['./old-users-list.component.scss']
})
export class OldUsersListComponent implements OnInit {
  anvandares: Anvandare[];

  constructor(private http: JwtHttp,
              private errorHandler: ErrorHandler,
              private stateService: StateService) { }

  ngOnInit() {
    this.http.get('/api/anvandare').map<Response, Anvandare[]>(response => response.json())
      .subscribe(
        anvandares => this.anvandares = anvandares
      );
  }

  impersonate(anvandare: Anvandare) {
    this.stateService.loggedInUser = anvandare;
  }
}
