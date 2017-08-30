import {Http, RequestOptionsArgs, Request, Response, XHRBackend} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Injectable} from '@angular/core';
import {Location} from '@angular/common';
import {RequestOptions, Headers, URLSearchParams} from '@angular/http';
import {AuthService} from './auth/auth.service';
import {ErrorHandler} from "../shared/error-handler";
import {StateService} from "./state/state.service";
import {Subscription} from "rxjs/Subscription";

@Injectable()
export class JwtHttp extends Http {

  authService: AuthService;
  errorHandler: ErrorHandler;
  stateService: StateService;
  location: Location;

  constructor(backend: XHRBackend,
              options: RequestOptions,
              authService: AuthService,
              errorHandler: ErrorHandler,
              stateService: StateService,
              location: Location) {
    const token = authService.jwt;

    if (token) {
      options.headers.set('Authorization', `Bearer ${token}`);
    }

    options.headers.set('If-Modified-Since', '0');

    super(backend, options);

    this.authService = authService;
    this.errorHandler = errorHandler;
    this.stateService = stateService;
    this.location = location;
  }

  getPage(url: string, page?: number, pageSize?: number) {
    let params: URLSearchParams = new URLSearchParams();
    params.set('page', page ? page.toString() : '0');
    params.set('pageSize', pageSize ? pageSize.toString() : '20');

    const options = new RequestOptions({search: params});

    return super.get(url, options)
  }

  request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {

    const token = this.authService.jwt;

    if (this.authService.isTokenExpired()) {
      this.location.go('/');
      this.authService.resetAuth();
      return Observable.of();
    }

    if (token) {
      if (typeof url === 'string') { // meaning we have to add the token to the options, not in url
        if (!options) {
          // let's make option object
          options = {headers: new Headers()};
        }
        options.headers.set('Authorization', `Bearer ${token}`);
      } else {
        // we have to add the token to the url object
        url.headers.set('Authorization', `Bearer ${token}`);
      }
    }

    const timerSubscription: Subscription = Observable.timer(250) // Delay when progress indicator is shown.
      .take(1)
      .subscribe(undefined, undefined, () => {
        this.stateService.startShowProgress();
      });

    return super.request(url, options)
      .catch(error => {
        this.errorHandler.notifyError(error);
        return Observable.throw(error);
      })
      .finally(() => {
        timerSubscription.unsubscribe();
        this.stateService.stopShowProgress()
      });
  }
}
