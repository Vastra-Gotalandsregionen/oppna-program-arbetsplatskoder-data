import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {AuthService} from "../../core/auth/auth.service";

@Injectable()
export class UserLoggedInGuard implements CanActivate {
  constructor(private authService: AuthService,
              private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    let authenticated = this.authService.isAuthenticated();

    if (!authenticated) {
      this.router.navigate(['/home']);
    }

    return authenticated;
  }
}
