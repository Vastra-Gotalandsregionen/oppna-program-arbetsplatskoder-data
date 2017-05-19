import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {StateService} from './state/state.service';
import {AdminGuard} from './guard/admin.guard';
import {AuthService} from './auth/auth.service';
import {JwtHttp} from './jwt-http';
import {HttpModule, XHRBackend, RequestOptions} from '@angular/http';
import {ErrorHandler} from "../shared/error-handler";
import {ConstantsService} from "./constants.service";
import 'rxjs/add/operator/timeout';
import 'rxjs/add/operator/combineLatest';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/concatMap';
import 'rxjs/add/operator/take';
import 'rxjs/add/operator/skip';
import 'rxjs/add/operator/toArray';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/distinct';
import 'rxjs/add/observable/from';
import 'rxjs/add/observable/timer';
import 'rxjs/add/operator/retry';
import 'rxjs/add/operator/retryWhen';
import 'rxjs/add/observable/interval';


@NgModule({
  imports: [
    CommonModule,
    HttpModule
  ],
  declarations: [],
  providers: [
    AdminGuard,
    AuthService,
    StateService,
    ConstantsService,
    {
      provide: JwtHttp,
      useFactory: JwtHttpFactory,
      deps: [XHRBackend, RequestOptions, AuthService, ErrorHandler, StateService]
    }
  ]
})
export class CoreModule { }

export function JwtHttpFactory(backend: XHRBackend, options: RequestOptions, authService: AuthService, errorHandler: ErrorHandler, stateService: StateService) {
  return new JwtHttp(backend, options, authService, errorHandler, stateService);
}
