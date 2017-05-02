import {Injectable} from '@angular/core';
import {JwtHelper} from "angular2-jwt/angular2-jwt";
import {Data} from "../../model/data";
import {Prodn1} from "../../model/prodn1";
import {Http} from "@angular/http";

@Injectable()
export class AuthService {

  private _jwt: string;
  private jwtToken: any;

  constructor(private jwtHelper: JwtHelper,
              private http: Http) {
    let localStorageToken = localStorage.getItem('apkJwtToken');

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
    }, 10000);
  }

  get jwt(): string {
    return this._jwt;
  }

  set jwt(value: string) {
    this._jwt = value;

    if (value) {
      this.jwtToken = this.jwtHelper.decodeToken(value);

      localStorage.setItem('apkJwtToken', value);

      console.log(this.jwtToken);
    } else if (this.jwtToken) {
      // todo Show dialog stating you've been logged out.
      this.jwtToken = null;
      localStorage.removeItem('apkJwtToken');
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
