import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {StateService} from './state/state.service';
import {AdminGuard} from './guard/admin.guard';
import {AuthService} from './auth/auth.service';
import {JwtHttp} from './jwt-http';
import {HttpModule, XHRBackend, RequestOptions} from '@angular/http';
import {ErrorHandler} from "../shared/error-handler";

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
