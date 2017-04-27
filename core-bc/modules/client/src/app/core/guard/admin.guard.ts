import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {StateService} from "../state/state.service";

@Injectable()
export class AdminGuard implements CanActivate {

  constructor(private stateService: StateService) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    const loggedInUser = this.stateService.loggedInUser;

    return loggedInUser && loggedInUser.behorighet === 2;
  }
}
