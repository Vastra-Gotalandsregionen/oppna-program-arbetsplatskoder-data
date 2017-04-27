import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {StateService} from "./state/state.service";
import {AdminGuard} from "./guard/admin.guard";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [],
  providers: [
    AdminGuard,
    StateService
  ]
})
export class CoreModule { }
