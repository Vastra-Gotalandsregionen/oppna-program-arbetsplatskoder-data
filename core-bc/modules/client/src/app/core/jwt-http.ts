import {Http, RequestOptionsArgs, Request, Response, XHRBackend} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Injectable} from '@angular/core';
import {RequestOptions, Headers} from '@angular/http';
import {AuthService} from './auth/auth.service';
import {ErrorHandler} from "../shared/error-handler";
import {StateService} from "./state/state.service";
import {Subscription} from "rxjs/Subscription";

@Injectable()
export class JwtHttp extends Http {

  authService: AuthService;
  errorHandler: ErrorHandler;
  stateService: StateService;

  constructor(backend: XHRBackend,
              options: RequestOptions,
              authService: AuthService,
              errorHandler: ErrorHandler,
              stateService: StateService) {
    const token = authService.jwt;

    if (token) {
      options.headers.set('Authorization', `Bearer ${token}`);
    }

    super(backend, options);

    this.authService = authService;
    this.errorHandler = errorHandler;
    this.stateService = stateService;
  }

  request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
    const token = this.authService.jwt;
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

    let timerSubscription: Subscription = Observable.timer(250)
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
