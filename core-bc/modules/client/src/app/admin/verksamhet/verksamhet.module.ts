import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VerksamhetListComponent } from './verksamhet-list/verksamhet-list.component';
import {VerksamhetRoutingModule} from "./verksamhet-routing.module";
import {SharedModule} from "../../shared/shared.module";
import { VerksamhetEditComponent } from './verksamhet-edit/verksamhet-edit.component';

@NgModule({
  imports: [
    CommonModule,
    VerksamhetRoutingModule,
    SharedModule
  ],
  declarations: [VerksamhetListComponent, VerksamhetEditComponent]
})
export class VerksamhetModule { }
