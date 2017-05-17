import {Injectable} from '@angular/core';
import {JwtHelper} from 'angular2-jwt/angular2-jwt';
import {Data} from '../../model/data';
import {Router} from '@angular/router';
import {Http} from '@angular/http';

@Injectable()
export class AuthService {

  private _jwt: string;
  private jwtToken: any;

  constructor(private jwtHelper: JwtHelper,
              private http: Http,
              private router: Router) {
    const localStorageToken = localStorage.getItem('apkJwtToken');

    if (localStorageToken) {
      this.jwt = localStorageToken;
    }

    setInterval(() => {
      if (this._jwt) {
        this.http.post('/api/login/renew', this._jwt)
          .subscribe(
            response => this.jwt = response.text(),
            error => this.jwt = null
          );
      }
    }, 60000);

    /*setInterval(() => {
      if (this._jwt) {
        this.http.post('/api/login/renew', this._jwt)
          .retryWhen(errors => errors.delay(10000))
          .take(10)
          .subscribe(
            response => this.jwt = response.text(),
            error => this.jwt = null
          );
      }
    }, 20000);*/
  }

  get jwt(): string {
    return this._jwt;
  }

  set jwt(value: string) {
    this._jwt = value;

    if (value) {
      this.jwtToken = this.jwtHelper.decodeToken(value);

      localStorage.setItem('apkJwtToken', value);
    } else if (this.jwtToken) {
      // Logout

      this.router.navigate(['/']);
      this.jwtToken = null;
      localStorage.removeItem('apkJwtToken');
    }

  }

  resetAuth() {
    this.jwt = null;
  }

  getLoggedInUserId(): string {
    return this.jwtToken ? this.jwtToken.sub : null;
  }

  getLoggedInDisplayName(): string {
    return this.jwtToken ? this.jwtToken.displayName : null;
  }

  getLoggedInRole() {
    return this.jwtToken ? this.jwtToken.roles : null;
  }

  userHasDataEditPermission(data: Data) {
    if (this.getLoggedInRole() === 'ADMIN') {
      return true;
    }

    if (this.jwtToken && this.jwtToken.prodn1s) {
      return this.jwtToken.prodn1s.indexOf(data.prodn1.id) > -1;
    }
    return false;
  }

}
