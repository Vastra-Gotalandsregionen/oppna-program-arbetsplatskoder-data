import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Prodn3ListComponent} from './prodn3-list/prodn3-list.component';
import {SharedModule} from '../shared/shared.module';
import {CoreModule} from '../core/core.module';
import {Prodn3RoutingModule} from './prodn3-routing.module';
import { Prodn3EditComponent } from './prodn3-edit/prodn3-edit.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    CoreModule,
    Prodn3RoutingModule
  ],
  declarations: [Prodn3ListComponent, Prodn3EditComponent]
})
export class Prodn3Module { }
