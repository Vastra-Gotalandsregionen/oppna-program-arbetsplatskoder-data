import { Injectable } from '@angular/core';
import {Anvandare} from '../../model/anvandare';

@Injectable()
export class StateService {

  private _loggedInUser: Anvandare;
  private _showDebug: boolean;
  showProgress: boolean;

  constructor() {}

  get loggedInUser(): Anvandare {
    return this._loggedInUser;
  }

  set loggedInUser(value: Anvandare) {
    this._loggedInUser = value;
  }

  get showDebug(): boolean {
    return this._showDebug;
  }

  set showDebug(value : boolean) {
    this._showDebug = value;
  }

  startShowProgress() {
    this.showProgress = true;
  }

  stopShowProgress() {
    this.showProgress = false;
  }
}
