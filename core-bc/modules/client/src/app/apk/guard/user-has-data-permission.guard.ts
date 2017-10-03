import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {AuthService} from '../../core/auth/auth.service';
import {JwtHttp} from '../../core/jwt-http';
import {Data} from '../../model/data';
import {Util} from "../../core/util/util";

@Injectable()
export class UserHasDataPermissionGuard implements CanActivate {

  constructor(private authService: AuthService,
              private http: JwtHttp,
              private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    let authorized = this.http.get('/api/data/' + next.params.id)
      .map(response => response.json())
      .map((data: Data) => this.authService.userHasDataEditPermission(data) && (!data.tillDatum
        || !Util.isOlderThanXYears(data.tillDatum, 0)))
      .do(authorized => {
        if (!authorized) {
          this.router.navigate(['/home']);
        }
      });

    return authorized;
  }

}
