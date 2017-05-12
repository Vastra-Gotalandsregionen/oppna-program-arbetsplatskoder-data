import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ao3ListComponent } from './ao3-list/ao3-list.component';
import {Ao3RoutingModule} from "./ao3-routing.module";
import {SharedModule} from "../../shared/shared.module";
import { Ao3EditComponent } from './ao3-edit/ao3-edit.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    Ao3RoutingModule
  ],
  declarations: [Ao3ListComponent, Ao3EditComponent]
})
export class Ao3Module { }
