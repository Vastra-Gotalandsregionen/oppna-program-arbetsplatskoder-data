import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {StateService} from "./state/state.service";
import {AdminGuard} from "./guard/admin.guard";
import {AuthService} from "./auth/auth.service";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [],
  providers: [
    AdminGuard,
    AuthService,
    StateService
  ]
})
export class CoreModule { }
