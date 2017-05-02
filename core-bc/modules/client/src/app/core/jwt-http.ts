import {Http, RequestOptionsArgs, Request, Response, XHRBackend} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Injectable} from "@angular/core";
import {RequestOptions, Headers} from "@angular/http";
import {AuthService} from "./auth/auth.service";

@Injectable()
export class JwtHttp extends Http {

  authService: AuthService;

  constructor (backend: XHRBackend, options: RequestOptions, authService: AuthService) {
    let token = authService.jwt;

    options.headers.set('Authorization', `Bearer ${token}`);
    super(backend, options);

    this.authService = authService;
  }

  request(url: string|Request, options?: RequestOptionsArgs): Observable<Response> {
    let token = this.authService.jwt;
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
    return super.request(url, options);
  }
}
