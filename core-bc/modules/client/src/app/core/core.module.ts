import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {StateService} from "./state/state.service";
import {AdminGuard} from "./guard/admin.guard";
import {AuthService} from "./auth/auth.service";
import {JwtHttp} from "./jwt-http";
import {HttpModule, XHRBackend, RequestOptions} from "@angular/http";

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
      useFactory: (backend: XHRBackend, options: RequestOptions, authService: AuthService) => {
        return new JwtHttp(backend, options, authService);
      },
      deps: [XHRBackend, RequestOptions, AuthService]
    }
  ]
})
export class CoreModule { }
