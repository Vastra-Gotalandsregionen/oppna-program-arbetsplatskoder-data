import {Injectable} from '@angular/core';
import {JwtHelper} from "angular2-jwt/angular2-jwt";
import {Data} from "../../model/data";
import {Router} from "@angular/router";
import {Http} from "@angular/http";

@Injectable()
export class AuthService {

  private _jwt: string;
  private jwtToken: any;

  constructor(private jwtHelper: JwtHelper,
              private http: Http,
              private router: Router) {
    let sessionStorageToken = sessionStorage.getItem('apkJwtToken');

    if (sessionStorageToken) {
      this.jwt = sessionStorageToken;
    }

    setInterval(() => {
      if (this._jwt) {
        this.http.post('/api/login/renew', this._jwt)
          .subscribe(
            response => this.jwt = response.text(),
            error => this.jwt = null
          );
      }
    }, 10000);
  }

  get jwt(): string {
    return this._jwt;
  }

  set jwt(value: string) {
    this._jwt = value;

    if (value) {
      this.jwtToken = this.jwtHelper.decodeToken(value);

      sessionStorage.setItem('apkJwtToken', value);
    } else if (this.jwtToken) {
      this.router.navigate(['/']);
      this.jwtToken = null;
      sessionStorage.removeItem('apkJwtToken');
    }


  }

  resetAuth() {
    this.jwt = null;
  }

  getLoggedInDisplayName(): string {
    return this.jwtToken ? this.jwtToken.displayName : null;
  }

  getLoggedInRole() {
    return this.jwtToken ? this.jwtToken.roles : null;
  }

  userHasDataEditPermission(data: Data) {
    // todo Is this the best way to check this? No more robust way than using "startsWith()"?
    if (this.jwtToken && this.jwtToken.prodn1s) {
      let matchArr: string[] = (<string[]>this.jwtToken.prodn1s)
        .filter(prodn1 => {
          return data.sorteringskodProd.startsWith(prodn1);
        });

      if (matchArr.length > 0) {
        return true;
      }
    }
    return false;
  }

}
