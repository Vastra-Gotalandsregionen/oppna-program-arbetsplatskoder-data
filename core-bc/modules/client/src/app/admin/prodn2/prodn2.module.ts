import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Prodn2ListComponent } from './prodn2-list/prodn2-list.component';
import { Prodn2EditComponent } from './prodn2-edit/prodn2-edit.component';
import {SharedModule} from '../../shared/shared.module';
import {Prodn2RoutingModule} from './prodn2-routing.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    Prodn2RoutingModule
  ],
  declarations: [Prodn2ListComponent, Prodn2EditComponent]
})
export class Prodn2Module { }
