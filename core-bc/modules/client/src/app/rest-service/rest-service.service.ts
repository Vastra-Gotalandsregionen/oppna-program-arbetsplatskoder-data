import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions, Response} from "@angular/http";
import {User} from "../model/user";
import {Observable} from "rxjs/Observable";
import {StateService} from "../core/state/state.service";

@Injectable()
export class RestServiceService {

  constructor(private http: Http,
              private stateService: StateService) { }

  // Example
  getUsers(): Observable<User[]> {

    // add authorization header with jwt token
    let headers = new Headers({ 'Authorization': 'Bearer ' + 'xxx' });
    let options = new RequestOptions({ headers: headers });

    return this.http.get('/api/users', options)
      .map<Response, User[]>((response: Response) => response.json());
  }

}
