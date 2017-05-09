import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {AuthService} from '../../core/auth/auth.service';
import {JwtHttp} from '../../core/jwt-http';
import {Data} from '../../model/data';

@Injectable()
export class UserHasDataPermissionGuard implements CanActivate {

  constructor(private authService: AuthService,
              private http: JwtHttp) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    return this.http.get('/api/data/' + next.params.id)
      .map(response => response.json())
      .map((data: Data) => this.userHasEditPermission(data));
  }

  userHasEditPermission(data: Data): boolean {
    return this.authService.userHasDataEditPermission(data);
  }
}
