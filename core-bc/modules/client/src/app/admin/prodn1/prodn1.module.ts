import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Prodn1ListComponent } from './prodn1-list/prodn1-list.component';
import {SharedModule} from '../../shared/shared.module';
import {Prodn1RoutingModule} from './prodn1-routing.module';
import { Prodn1EditComponent } from './prodn1-edit/prodn1-edit.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    Prodn1RoutingModule
  ],
  declarations: [Prodn1ListComponent, Prodn1EditComponent]
})
export class Prodn1Module { }
