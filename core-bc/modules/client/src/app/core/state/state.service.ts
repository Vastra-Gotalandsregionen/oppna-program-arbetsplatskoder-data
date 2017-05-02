import { Injectable } from '@angular/core';
import {Anvandare} from "../../model/anvandare";

@Injectable()
export class StateService {

  private _loggedInUser: Anvandare;
  showProgress: boolean;

  constructor() {}

  get loggedInUser(): Anvandare {
    return this._loggedInUser;
  }

  set loggedInUser(value: Anvandare) {
    this._loggedInUser = value;
  }

  startShowProgress() {
    this.showProgress = true;
  }

  stopShowProgress() {
    this.showProgress = false;
  }
}
