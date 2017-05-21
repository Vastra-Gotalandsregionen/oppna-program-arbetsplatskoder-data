import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VardformListComponent } from './vardform-list/vardform-list.component';
import {VardformRoutingModule} from "./vardform-routing.module";
import {SharedModule} from "../../shared/shared.module";
import { VardformEditComponent } from './vardform-edit/vardform-edit.component';

@NgModule({
  imports: [
    CommonModule,
    VardformRoutingModule,
    SharedModule
  ],
  declarations: [VardformListComponent, VardformEditComponent]
})
export class VardformModule { }
